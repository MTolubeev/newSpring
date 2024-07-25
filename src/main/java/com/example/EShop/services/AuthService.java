package com.example.EShop.services;


import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public String createAuthToken(String email, String password,Model model) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRepository.findByEmail(email).getUsername(), password));
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Неправильный логин или пароль");
            return "login";
        }
        UserDetails userDetails = userService.loadUserByUsername(userRepository.findByEmail(email).getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        User user = userRepository.findByUsername(userRepository.findByEmail(email).getUsername());

        model.addAttribute("token", token);
        model.addAttribute("user", user);
        System.out.println("token : " + token);
        return "redirect:/";
    }
}