package com.example.EShop.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String email;
    private String surname;
    private String password;
}