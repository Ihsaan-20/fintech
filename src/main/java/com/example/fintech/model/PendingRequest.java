package com.example.fintech.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pending_requests")
public class PendingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String type; // OTP_VERIFICATION, T_PIN_VERIFICATION, BANK_TRANSFER

    private String receiver; // Mobile number, IBAN, etc.

    private BigDecimal amount;

    private String otp;

    private String tPin;

    private String status; // PENDING, VERIFIED, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();
}
