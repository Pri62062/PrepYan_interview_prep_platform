package com.prep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * HealthController — Render cold start ke liye
 *
 * Frontend is endpoint ko ping karta hai jab page load ho.
 * Isse backend "jaga" rehta hai aur 502 kam aata hai.
 *
 * Endpoint: GET /api/health
 * Response: { "status": "UP", "time": "..." }
 */
@RestController

public class HealthController {

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status",  "UP",
            "service", "PrepYan API",
            "time",    LocalDateTime.now().toString()
        ));
    }

    // ✅ Root URL redirect — prevents 404 on base URL
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("PrepYan Backend is running ✅ — API at /api/");
    }
}