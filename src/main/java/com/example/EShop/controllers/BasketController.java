package com.example.EShop.controllers;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("/{userId}")
    public String getBasket(@PathVariable Long userId, Model model) {
        try {

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            List<Product> products = basketService.getUserProducts(user);
            model.addAttribute("products", products);
            model.addAttribute("user", user);

            return "basket";
        } catch (Exception e) {
            model.addAttribute("message", "Error fetching basket: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/{userId}")
    public String addToBasket(@RequestParam Long productId, @PathVariable Long userId, Model model, @RequestParam String token) {


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

        return "redirect:/?token=" + token;
    }

    @PostMapping("/{userId}/delete/{productId}")
    public String deleteFromBasket(@PathVariable Long userId, @PathVariable Long productId, Model model) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            basketService.deleteProduct(user, product);
            model.addAttribute("message", "Product removed from basket successfully");
        } catch (Exception e) {
            model.addAttribute("message", "Error removing product from basket: " + e.getMessage());
        }
        return "redirect:/basket/" + userId;

    }

}