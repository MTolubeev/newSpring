package com.example.EShop.controllers;

import com.example.EShop.dtos.ProductDto;
import com.example.EShop.models.Comment;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.CommentService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;
    private final BasketService basketService;
    private final UserService userService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestHeader(value = "Authorization", required = false) String token,
                           Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("comments", commentService.findAll());
        if (token != null) {
            model.addAttribute("token", token);
            User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
            model.addAttribute("user", user);
            model.addAttribute("basketSize", basketService.returnBasketSize(user));
            model.addAttribute("firstLetterName", userService.returnFirstLetter(user));
        }
        return "products";
    }

    //    @GetMapping("/product/getAll")
//    public ResponseEntity<List<Product>> getAll(@RequestParam(name = "title", required = false) String title) {
//        return ResponseEntity.ok(productService.listProducts(title));
//    }
    @GetMapping("/product/getAll")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                Product product) throws IOException {
        productService.saveProduct(product, file1);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}