package com.example.EShop.services;

import com.example.EShop.dtos.JwtRequest;
import com.example.EShop.dtos.JwtResponse;
import com.example.EShop.dtos.RegistrationUserDto;
import com.example.EShop.exceptions.AppError;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public String createAuthToken(@RequestBody JwtRequest authRequest,Model model) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Неправильный логин или пароль");
            return "login";
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        User user = userRepository.findByUsername(authRequest.getUsername());

        model.addAttribute("token", token);
        model.addAttribute("user", user);
        System.out.println("token : " + token);
        return "redirect:/?token=" + token;
    }


}