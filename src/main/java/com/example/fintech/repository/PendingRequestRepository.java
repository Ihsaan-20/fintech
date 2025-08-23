package com.example.fintech.repository;

import com.example.fintech.model.PendingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingRequestRepository extends JpaRepository<PendingRequest, Long> {
    Optional<PendingRequest> findByUserIdAndTypeAndStatus(Long userId, String type, String status);
}
