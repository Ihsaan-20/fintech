package com.example.fintech.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionOtpDTO {
    private Long userId;
    private String receiverMobileNumber;
    private BigDecimal amount;

    // Getters and setters
}
