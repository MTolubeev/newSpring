package com.example.EShop.controllers;

import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;
    private final BasketService basketService;
    private final UserService userService;
    private  final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "token", required = false) String token,
                           Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));

        if (token != null) {
            model.addAttribute("token", token);
            User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
            model.addAttribute("user", user);
            model.addAttribute("basketSize", basketService.returnBasketSize(user));
            model.addAttribute("firstLetterName", userService.returnFirstLetter(user));
        }
        return "products";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, Product product) throws IOException {
        productService.saveProduct(product, file1);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}