package com.example.EShop.controllers;

import com.example.EShop.models.Product;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {

        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        if (productService.getUserByPrincipal(principal).getEmail() != null) {
            model.addAttribute("basketSize", basketService.returnBasketSize(productService.getUserByPrincipal(principal)));
            model.addAttribute("firstLetterName", userService.returnFirstLetter(productService.getUserByPrincipal(principal)));
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
