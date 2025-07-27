package com.example.fintech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transaction_otp")
public class TransactionOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String receiverInput;
    private String transferType;
    private String bankName;
    private BigDecimal amount;

    private String otp;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

}

