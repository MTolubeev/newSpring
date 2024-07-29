package com.example.EShop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductDto {
        private Long id;
        private String title;
        private String description;
        private int count;
        private Long price;
        private Long discountPrice;
    }
