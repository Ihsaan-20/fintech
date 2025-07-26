package com.example.fintech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private BigDecimal amount;

    private String note;

    private LocalDateTime timestamp;

    // Constructor with all fields
    public Transaction(User sender, User receiver, BigDecimal amount, String note) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.note = note;
        this.timestamp = LocalDateTime.now();
    }
}