package com.example.fintech.dto;

import lombok.Data;

@Data
public class VerifyReceiverRequestDTO {
    private String transferType; // fintech | bank
    private String bankName;     // optional if fintech
    private String receiverInput; // number ya IBAN
}