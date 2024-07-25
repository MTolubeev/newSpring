package com.example.EShop.controllers;


import com.example.EShop.models.Basket;
import com.example.EShop.models.Order;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.DefaultEmailService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final DefaultEmailService emailService;
    private final BasketService basketService;

    @GetMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestHeader(value = "Authorization", required = false) String token) {
        User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
        List<Product> usersProducts = basketService.getUserProducts(user);
        int totalPrice = 0;
        List<Order> fullOrder = new ArrayList<Order>() ;


        Set<Product> set = new HashSet<>();
        List<Product> duplicates = new ArrayList<>();
        usersProducts.forEach(n -> {
            if (!set.add(n)) {
                duplicates.add(n);
            }
        });

        for (int k = 0; k < usersProducts.size(); k++) {
            Order orderPart = new Order();

           orderPart.setNameOfProduct(usersProducts.get(k).getTitle());
           orderPart.setPrice(usersProducts.get(k).getPrice());
            if(duplicates.contains(usersProducts.get(k))){
               orderPart.setCountOfProducts(orderPart.getCountOfProducts() + 1);
            }
            if(!fullOrder.contains(orderPart)) {
                fullOrder.add(orderPart);
            }

        }

        String address = "lector1774@gmail.com";
        emailService.sendSimpleEmail(
                address,
                "Ваш заказ на E-shop:",
                fullOrder
        );
        return ResponseEntity.ok("all right");
    }
}
