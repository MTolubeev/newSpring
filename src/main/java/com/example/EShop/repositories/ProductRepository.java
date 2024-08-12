package com.example.EShop.repositories;

import com.example.EShop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

  //  Product findById(Long id);

    List<Product> findAll();

}
