package com.example.EShop.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
