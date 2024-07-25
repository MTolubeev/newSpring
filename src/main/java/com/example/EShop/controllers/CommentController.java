package com.example.EShop.controllers;

import com.example.EShop.models.Comment;
import com.example.EShop.models.CommentImage;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.CommentRepository;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.CommentService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    @PostMapping("/addComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(@RequestParam("productId") Long productId,
                                              @RequestParam("text") String text,
                                              @RequestParam("score") int score,
                                              @RequestParam(value = "image", required = false) MultipartFile image,
                                              @RequestHeader("Authorization") String token) throws IOException {

        return commentService.addComment(productId, text, score, image,token);
    }

    @GetMapping("/comment/{productId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(commentService.findByProduct(product));
    }

//    @GetMapping("/product/{productId}/score/{score}")
//    public ResponseEntity<List<Comment>> getCommentsByScore(@PathVariable Long productId, @PathVariable int score) {
//        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//        return ResponseEntity.ok(commentService.findByProductAndScore(product, score));
//    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentService.delete(comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }


}
