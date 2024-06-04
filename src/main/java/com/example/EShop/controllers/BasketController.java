package com.example.EShop.controllers;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("/basket/{userId}")
    public String getBasket(@PathVariable Long userId, Model model){
     if (userId == null) {
        model.addAttribute("message", "User ID not found in session");
        return "errorPage";
    }

    User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
        model.addAttribute("message", "User not found");
        return "errorPage";
    }

    List<Product> products = basketService.getUserProducts(user);
        model.addAttribute("products", products);
        return "/basket";
}

    @PostMapping("/basket/adding")
    public String addToBasket(@RequestParam Long productId, @RequestParam Long userId, Model model) {


        if (userId == null) {
            model.addAttribute("message", "User ID not found in session");
            return "errorPage";
        }

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

            basketService.addProduct(user, product);
            model.addAttribute("message", "Product added to basket successfully");
        } catch (Exception e) {
            model.addAttribute("message", "Error adding product to basket: " + e.getMessage());
        }

        return "/";
    }
}
