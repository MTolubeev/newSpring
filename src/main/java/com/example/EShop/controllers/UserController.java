package com.example.EShop.controllers;

import com.example.EShop.dtos.JwtRequest;
import com.example.EShop.dtos.RegistrationUserDto;
import com.example.EShop.dtos.UserDto;

import com.example.EShop.services.AuthService;
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


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;


    @PostMapping("/login")
    public String auth(@RequestBody JwtRequest authRequest, Model model) {

        return authService.createAuthToken(authRequest, model);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registration")

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {

        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}