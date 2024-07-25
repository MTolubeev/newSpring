package com.example.EShop.controllers;

import com.example.EShop.dtos.UserDto;

import com.example.EShop.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.EShop.models.User;
import com.example.EShop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/login")
    public String auth(@RequestParam(name = "email") String email ,@RequestParam(name = "password") String password, Model model) {
        return authService.createAuthToken(email, password, model);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registration")

    public ResponseEntity<?> createNewUser(@RequestParam(name = "username") String username,
                                           @RequestParam(name = "surname") String surname,
                                           @RequestParam(name = "email") String email,
                                           @RequestParam(name = "password") String password) {
        User user = userService.createNewUser(username, surname, email, password);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}