package com.example.fintech.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CompleteTransferDTO {
    private String receiverInput;
    private BigDecimal amount;
    private String otp;
    private String transferType;  // FINTECH or BANK
    private String bankName;      // if BANK
}
