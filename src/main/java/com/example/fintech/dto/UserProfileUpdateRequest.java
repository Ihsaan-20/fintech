package com.example.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    @NotBlank
    @Size(max = 40)
    private String firstName;

    @Size(max = 40)
    private String middleName;

    @NotBlank
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be exactly 11 digits")
    private String mobileNumber;  // To identify which user to update
}
