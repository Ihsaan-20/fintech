package com.example.fintech.dto;

import com.example.fintech.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
    private String accessToken;
    private User user;

    // getters and setters
}
