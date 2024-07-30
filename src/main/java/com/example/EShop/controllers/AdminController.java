package com.example.EShop.controllers;

import com.example.EShop.models.User;
import com.example.EShop.models.enums.Role;
import com.example.EShop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> admin() {
        List<User> users = userService.list();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user/ban/{id}")
    public ResponseEntity<Void> userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return ResponseEntity.ok().build();
    }


}
