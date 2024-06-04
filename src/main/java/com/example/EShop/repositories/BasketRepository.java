package com.example.EShop.repositories;

import com.example.EShop.models.Basket;
import com.example.EShop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByUser(User user);
}
