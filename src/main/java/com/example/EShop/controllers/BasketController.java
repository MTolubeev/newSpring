package com.example.EShop.controllers;

import com.example.EShop.models.Basket;
import com.example.EShop.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BasketController {
    @GetMapping("/basket")
    public String basket() {
        return "basket";
    }
   @PostMapping("/basket")
  public String saveProductToBasket(User user,Long id){
       Basket basket = new Basket();
return "redirect:/prodect";
   }
}
