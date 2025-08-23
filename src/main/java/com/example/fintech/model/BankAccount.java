package com.example.fintech.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bank_accounts")
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    @Column(name = "accountNumber") // match actual DB column name
    private String accountNumber;

    @Column(name = "ibanNumber")
    private String ibanNumber;

    private String accountHolderName;

    // No-argument constructor (required by JPA)
    public BankAccount() {
    }

    // Constructor without id (for creating new records)
    public BankAccount(String bankName, String accountNumber, String ibanNumber, String accountHolderName) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ibanNumber = ibanNumber;
        this.accountHolderName = accountHolderName;
    }

    // Optional: constructor with id (not generally used if id is auto-generated)
    public BankAccount(Long id, String bankName, String accountNumber, String ibanNumber, String accountHolderName) {
        this.id = id;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ibanNumber = ibanNumber;
        this.accountHolderName = accountHolderName;
    }
}
