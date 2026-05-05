package com.prep.controller;

import com.prep.entity.User;
import com.prep.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UserService service;

    // 🔥 REGISTER (CLEAN)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("REGISTER HIT: " + user.getEmail() + " ROLE: " + user.getRole());

            User savedUser = service.register(user);

            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // 🔥 LOGIN (NO JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            System.out.println("LOGIN HIT: " + email);

            User user = service.login(email, password);

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}