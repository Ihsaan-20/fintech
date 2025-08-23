package com.example.fintech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotBlank(message = "Receiver mobile number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Receiver mobile number must be exactly 11 digits")
    private String receiverMobileNumber;

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String note; // optional
}
