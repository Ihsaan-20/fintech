package com.example.fintech.model;

public enum TransactionType {
    CREDIT,           // Paise aaye hain
    DEBIT,            // Paise gaye hain
    IBFT,             // Inter Bank Funds Transfer
    RAAST,            // RAAST ID se transaction
    WALLET_TRANSFER,  // App ke andar wallet se bheja ya received
    CASH_DEPOSIT,     // Cash deposit
    BILL_PAYMENT,     // Bill waghera pay
    OTHER             // Misc.
}
