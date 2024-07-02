package com.example.EShop.controllers;

import com.example.EShop.services.ProductService;
import org.springframework.ui.Model;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ProductService productService;


    @PostMapping("/login")
    public String auth(@RequestParam String username, @RequestParam String password, Model model, Principal principal) {
        try {
            System.out.println("Authentication attempt for user: " + username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed for user: " + username);
            System.out.println("Неправильный логин или пароль");
            return "login";
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenUtils.generateToken(userDetails);
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        System.out.println("токен: " + token);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
