package com.example.EShop.controllers;

import com.example.EShop.models.Comment;
import com.example.EShop.models.Product;
import com.example.EShop.repositories.CommentRepository;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.services.CommentService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ProductService productService;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(@RequestParam("productId") Long productId,
                                              @RequestParam("text") String text,
                                              @RequestParam("score") int score,
                                              @RequestParam(value = "image", required = false) MultipartFile image,
                                              @RequestHeader("Authorization") String token) throws IOException {
        return commentService.addComment(productId, text, score, image, token);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(commentService.findByProduct(product));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentService.delete(comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }
}
