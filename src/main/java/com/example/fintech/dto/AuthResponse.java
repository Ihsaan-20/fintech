package com.example.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer"; // Default value
    private Long userId;
    private String email;
    private String name;

    // Constructor without tokenType (it will use the default)
    public AuthResponse(String accessToken, Long userId, String email, String name) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}