package com.example.EShop.repositories;

import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Product, User> {
}
