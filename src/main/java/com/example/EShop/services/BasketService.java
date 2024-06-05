package com.example.EShop.services;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
@Transactional
    public void addProduct(User user, Product product) {
        // Поиск существующей корзины для пользователя

        Basket basket = basketRepository.findByUser(user);

        // Если корзина не найдена, создать новую
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
}
