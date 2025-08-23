package com.example.fintech.dto;

import com.example.fintech.model.TransactionStatus;
import com.example.fintech.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private String note;
    private TransactionType transactionType;
    private TransactionStatus status;
    private BigDecimal balanceAfterTransaction;
    private LocalDateTime timestamp;
    private Long senderId;
    private String senderName;       // ✅ new field
    private Long receiverId;
    private String receiverName;     // ✅ new field
}