package com.example.fintech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "verification_codes")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String mobileNumber;

    @Column(length = 5)
    private String countryCode;

    @Column(nullable = false, length = 6)
    private String code; // 6 digit OTP

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private int sendCodeCount = 0;

    @Column(length = 50)
    private String codeFor;  // e.g. "registration"

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
