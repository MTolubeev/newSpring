package com.example.EShop.controllers;

import com.example.EShop.dtos.ProductOrderDto;
import com.example.EShop.models.User;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.DefaultEmailService;
import com.example.EShop.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final DefaultEmailService emailService;
    private final BasketService basketService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestHeader(value = "Authorization", required = false) String token) {
        User user = userRepository.findByUsername(jwtTokenUtils.getUsername(token));
        List<ProductOrderDto> usersProducts = basketService.getUserProductDtos(user);

        int totalPrice = 0;
        StringBuilder emailContent = new StringBuilder("Ваш заказ на E-shop:\n\n");

        for (ProductOrderDto productOrder : usersProducts) {
            emailContent.append(productOrder.getTitle())
                    .append(" - Количество: ").append(productOrder.getCount())
                    .append(" - Цена: ").append(productOrder.getPrice() * productOrder.getCount())
                    .append("\n");
            totalPrice += (int) (productOrder.getPrice() * productOrder.getCount());
        }

        emailContent.append("\nИтоговая стоимость: ").append(totalPrice).append(" руб.");

        String address = user.getEmail();
        emailService.sendSimpleEmail(
                address,
                "Ваш заказ на E-shop",
                emailContent.toString()
        );

        for (ProductOrderDto productOrder : usersProducts){
            basketService.cleanBasketAfterEmail(user,productOrder.getId());
        }

        return ResponseEntity.ok("Email sent successfully");
    }
}