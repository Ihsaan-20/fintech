package com.example.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be exactly 11 digits")
    private String mobileNumber;

    @Size(max = 40)
    private String firstName;  // optional

    @Size(max = 40)
    private String middleName; // optional

    @Size(max = 40)
    private String lastName;   // optional

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;   // optional during OTP verification

}
