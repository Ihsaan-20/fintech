package com.example.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be exactly 11 digits")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
