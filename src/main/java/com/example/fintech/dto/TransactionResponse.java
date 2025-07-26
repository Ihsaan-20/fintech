package com.example.fintech.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private String senderName;
    private String receiverName;
    private String amount;
    private LocalDateTime timestamp;
    private String note;
}