package com.example.gateway_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class FallbackController{
    @GetMapping("/fallback/users")
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/fallback/notifications")
    public ResponseEntity<String> notificationServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Notification Service is temporarily unavailable. Please try again later.");
    }
}
