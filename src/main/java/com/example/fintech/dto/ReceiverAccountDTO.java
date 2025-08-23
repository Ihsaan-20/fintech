package com.example.fintech.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverAccountDTO {

    private String bankName; // For fintech you can put "FINTECH"
    private String accountNumber; // mobile number or IBAN/account number
    private String accountHolderName;
}