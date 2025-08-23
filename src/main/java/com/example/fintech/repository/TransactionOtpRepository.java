package com.example.fintech.repository;


import com.example.fintech.model.TransactionOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionOtpRepository extends JpaRepository<TransactionOtp, Long> {
    Optional<TransactionOtp> findTopByUserIdAndReceiverInputOrderByCreatedAtDesc(Long userId, String receiverInput);

}
