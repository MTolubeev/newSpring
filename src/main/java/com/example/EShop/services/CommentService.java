package com.example.EShop.services;

import com.example.EShop.dtos.CommentDto;
import com.example.EShop.models.Comment;
import com.example.EShop.models.CommentImage;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.CommentRepository;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.utils.JwtTokenUtils;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final ProductRepository productRepository;

    public ResponseEntity<Comment> addComment(Long productId, String text, int score, MultipartFile image, String token) throws IOException {
        User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setText(text);
        comment.setScore(score);

        if (image != null && !image.isEmpty()) {
            // Save the image and set the URL
            String imageUrl = saveImage(image);
            CommentImage commentImage = new CommentImage();
            commentImage.setImageUrl(imageUrl);
            commentImage.setComment(comment);
            comment.getImages().add(commentImage);

        }

        Comment savedComment = save(comment);
        return ResponseEntity.ok(savedComment);
    }


    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findByProduct(Product product) {
        return commentRepository.findByProduct(product);
    }


    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
    public void deleteImage(Comment comment) {
            comment.setImages(null);
            commentRepository.save(comment);
    }
    public String saveImage(MultipartFile image) throws IOException {
        String filename = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
        Path imagePath = Paths.get("images", filename);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images/" + filename;
    }

}
