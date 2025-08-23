package com.example.fintech.repository;

import com.example.fintech.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByMobileNumberOrderByCreatedAtDesc(String mobileNumber);
    long countByMobileNumberAndCreatedAtBetween(String mobileNumber, LocalDateTime start, LocalDateTime end);
}
