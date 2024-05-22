package com.example.EShop.services;

import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final List<Product> products = new ArrayList<>();

    public void addProduct(User user, Product product){

    }
}
