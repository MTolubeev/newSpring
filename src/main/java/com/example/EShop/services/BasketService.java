package com.example.EShop.services;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

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
    public List<Product> getUserProducts(User user) {
        Basket basket = basketRepository.findByUser(user);
        if (basket != null && basket.getProducts() != null) {
            return basket.getProducts();
        }
        return new ArrayList<>();
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
    public int returnBasketSize(User user) {
        Basket basket = basketRepository.findByUser(user);
        if (basket == null) {
            basket = new Basket();
        }
        List<Product> productsInBasket = basket.getProducts();

        return productsInBasket.size();
    }

}
