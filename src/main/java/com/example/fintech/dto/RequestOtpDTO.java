package com.example.fintech.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestOtpDTO {
    private String receiverInput; // mobile number or IBAN/account
    private String transferType;  // FINTECH or BANK
    private String bankName;      // if BANK
    private BigDecimal amount;
}
