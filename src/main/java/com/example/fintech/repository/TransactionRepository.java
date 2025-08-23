package com.example.fintech.repository;


import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrReceiverOrderByTimestampDesc(User sender, User receiver);

    // ✅ Sent transactions
    List<Transaction> findBySenderAndTimestampBetweenOrderByTimestampDesc(
            User sender, LocalDateTime start, LocalDateTime end
    );

    // ✅ Received transactions
    List<Transaction> findByReceiverAndTimestampBetweenOrderByTimestampDesc(
            User receiver, LocalDateTime start, LocalDateTime end
    );

    @Query("""
        SELECT t FROM Transaction t 
        WHERE (t.sender = :user OR t.receiver = :user)
        AND t.timestamp BETWEEN :start AND :end
        ORDER BY t.timestamp DESC
    """)
    List<Transaction> findAllByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Transaction> findBySenderOrReceiverAndTimestampBetweenOrderByTimestampDesc(
            User sender, User receiver, LocalDateTime start, LocalDateTime end);

    List<Transaction> findAllBySenderOrReceiverAndTimestampBetween(
            User sender, User receiver, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE (t.sender = :user OR t.receiver = :user) " +
            "AND t.timestamp BETWEEN :start AND :end")
    List<Transaction> findUserTransactionsWithinMonth(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);



}