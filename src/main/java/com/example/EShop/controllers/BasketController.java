package com.example.EShop.controllers;

import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping("/basket")
    public String basket() {
        return "basket";
    }

    @PostMapping("/basket/adding")
    public String addToBasket( @ModelAttribute Product product, @ModelAttribute User user) {


        basketService.addProduct(user, product);


        return "basket";
    }
}
