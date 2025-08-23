package com.example.fintech.dto;

import lombok.Data;

@Data
public class VerifyReceiverDTO {

    private String type; // e.g., "FINTECH" or "BANK"

    private String mobileNumber; // used if type is FINTECH

    private String bankName; // used if type is BANK

    private String account; // used if type is BANK

}