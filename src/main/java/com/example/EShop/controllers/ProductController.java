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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final BasketService basketService;
    private final UserService userService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/")
    public ResponseEntity<?> products(@RequestParam(name = "title", required = false) String title,
                                      @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            List<ProductDto> products = productService.findAllProducts();
            List<Comment> comments = commentService.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("comments", comments);

            if (token != null && !token.isEmpty()) {
                User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
                response.put("user", user);
                response.put("basketSize", basketService.returnBasketSize(user));
                response.put("firstLetterName", userService.returnFirstLetter(user));
                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(response);
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }

    @GetMapping("/product/getAll")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/product/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestParam("file1") MultipartFile file1,
                                           @RequestBody Product product,
                                           @RequestHeader(value = "Authorization", required = false) String token) throws IOException {
        productService.saveProduct(product, file1);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @PostMapping("/product/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
