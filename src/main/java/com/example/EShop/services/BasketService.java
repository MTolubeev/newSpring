package com.example.EShop.services;

import com.example.EShop.dtos.ProductOrderDto;
import com.example.EShop.models.Basket;
import com.example.EShop.models.Image;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import com.example.EShop.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ImageRepository imageRepository;

    public List<ProductOrderDto> getUserProductDtos(User user) {
        Basket basket = basketRepository.findByUser(user);
        if (basket != null && basket.getProducts() != null) {
            return convertToProductOrderDtos(basket.getProducts());
        }
        return new ArrayList<>();
    }

    private List<ProductOrderDto> convertToProductOrderDtos(List<Product> products) {
        Map<Long, ProductOrderDto> productOrderDtoMap = new HashMap<>();

        for (Product product : products) {
            Image image = imageRepository.findById(product.getPreviewImageId()).orElse(null);
            String base64Image = image != null ? Base64.getEncoder().encodeToString(image.getBytes()) : null;
            ProductOrderDto dto = productOrderDtoMap.getOrDefault(product.getId(),
                    new ProductOrderDto(product.getId(),
                            product.getTitle(),
                            product.getDiscountPrice(),
                            0,
                            base64Image));
            dto.setCount(dto.getCount() + 1);
            productOrderDtoMap.put(product.getId(), dto);
        }

        return new ArrayList<>(productOrderDtoMap.values());
    }

    @Transactional
    public void addProduct(User user, Product product) {
        Basket basket = basketRepository.findByUser(user);
        if (basket == null) {
            basket = new Basket();
            basket.setUser(user);
        }

        List<Product> products = basket.getProducts();
        if (products == null) {
            products = new ArrayList<>();
            basket.setProducts(products);
        }

        products.add(product);
        // меняем колличество товара на - 1
        product.setCount(product.getCount() - 1);
        basketRepository.save(basket);
    }


    @Transactional
    public void deleteProduct(User user, Product product) {
        Basket basket = basketRepository.findByUser(user);
        List<Product> products = basket.getProducts();
        products.remove(product);
        product.setCount(product.getCount() + 1);
        basketRepository.save(basket);

    }

    @Transactional
    public void deleteFullProduct(User user, Product product) {
        Basket basket = basketRepository.findByUser(user);
        List<Product> products = basket.getProducts();
        while (products.contains(product)) {
            products.remove(product);

            product.setCount(product.getCount() + 1);
        }
        basketRepository.save(basket);

    }

    @Transactional
    public int returnBasketSize(User user) {
        Basket basket = basketRepository.findByUser(user);
        if (basket == null) {
            basket = new Basket();
        }
        List<Product> productsInBasket = basket.getProducts();

        return productsInBasket.size();
    }

}
