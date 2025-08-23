package com.example.fintech.service;


import com.example.fintech.dto.TransactionDto;
import com.example.fintech.dto.TransactionResponseDto;
import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import com.example.fintech.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public TransactionResponseDto getCurrentMonthTransactions(User user) {
        // Current month range
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);

        // Fetch transactions (sender or receiver)
        List<Transaction> transactions = transactionRepository.findUserTransactionsWithinMonth(
                user, startOfMonth, startOfNextMonth);

        // Map to DTO
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(t -> new TransactionDto(
                        t.getId(),
                        t.getTransactionId(),
                        t.getAmount(),
                        t.getNote(),
                        t.getTransactionType(),
                        t.getStatus(),
                        t.getBalanceAfterTransaction(),
                        t.getTimestamp(),
                        t.getSender() != null ? t.getSender().getId() : null,
                        t.getSender() != null ? t.getSender().getFirstName()+" "+ t.getSender().getLastName(): null,
                        t.getReceiver() != null ? t.getReceiver().getId() : null,
                        t.getReceiver() != null ? t.getReceiver().getFirstName() +" "+ t.getReceiver().getLastName(): null
                ))
                .collect(Collectors.toList());
        String currentMonthWithYear = startOfMonth.getMonth().name() + " " + startOfMonth.getYear();

        // Return wrapper with count
        return new TransactionResponseDto(transactionDtos.size(), currentMonthWithYear, transactionDtos);
    }












}
