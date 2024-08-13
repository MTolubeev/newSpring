package com.example.EShop.dtos;

import com.example.EShop.models.Comment;
import com.example.EShop.models.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthDto {
    private Long uid;
    private String email;
    private String username;
    private String surname;
    private String role;
    private List<Comment> comments;
    private String token;

    public AuthDto(User user, String token) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.surname = user.getSurname();
        this.role = user.getRoles().toString().replace("[", "").replace("]", "");
        this.comments = user.getComments();

        this.token = token;
    }
}
