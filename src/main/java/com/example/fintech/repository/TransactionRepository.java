package com.example.fintech.repository;


import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrReceiverOrderByTimestampDesc(User sender, User receiver);
}