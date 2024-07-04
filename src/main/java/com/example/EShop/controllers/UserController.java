package com.example.EShop.controllers;

import com.example.EShop.dtos.JwtResponse;
import com.example.EShop.exceptions.AppError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public String auth(@RequestParam String username, @RequestParam String password, Principal principal, Model model) {
        try {
            System.out.println("Authentication attempt for user: " + username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed for user: " + username);
            System.out.println("Неправильный логин или пароль");
            new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
            return "/login";
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenUtils.generateToken(userDetails);
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        System.out.println("токен: " + token);
        ResponseEntity.ok(new JwtResponse(token));

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
