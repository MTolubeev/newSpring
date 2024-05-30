package com.example.EShop.services;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ArrayList<Product> products = new ArrayList<>();

    public void addProduct(@RequestParam User user, Product product) {
        Basket basket = new Basket();
        basket.setProducts(product);
        basket.setUser(user);
    }
}
