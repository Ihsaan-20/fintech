package com.example.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendOtp {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be exactly 11 digits")
    private String mobileNumber;

    @Size(max = 2, message = "Country code must be maximum 2 characters")
    private String country;  // optional
}
