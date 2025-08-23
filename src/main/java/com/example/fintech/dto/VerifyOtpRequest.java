package com.example.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be exactly 11 digits")
    private String mobileNumber;

    @NotBlank
    @Size(min = 6, max = 6)
    private String otp;

//  for transaction verification
    private Long userId;
}
