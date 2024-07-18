package com.example.EShop.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
