package com.example.fintech.service;

import com.example.fintech.dto.TransactionRequest;
import com.example.fintech.dto.TransactionResponse;
import com.example.fintech.exception.BadRequestException;
import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import com.example.fintech.repository.TransactionRepository;
import com.example.fintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public TransactionResponse sendMoney(Long senderId, TransactionRequest transactionRequest) {
        User sender = userService.getUserById(senderId);
        User receiver = userRepository.findByEmail(transactionRequest.getReceiverEmail())
                .orElseThrow(() -> new BadRequestException("Receiver not found"));

        if (sender.getEmail().equals(receiver.getEmail())) {
            throw new BadRequestException("Cannot send money to yourself");
        }

        BigDecimal amount = transactionRequest.getAmount();
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        // Update balances
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Save users
        userRepository.save(sender);
        userRepository.save(receiver);

        // Create and save transaction
        Transaction transaction = new Transaction(sender, receiver, amount, transactionRequest.getNote());
        transactionRepository.save(transaction);

        return mapToTransactionResponse(transaction);
    }

    public List<TransactionResponse> getUserTransactions(Long userId) {
        User user = userService.getUserById(userId);
        return transactionRepository.findBySenderOrReceiverOrderByTimestampDesc(user, user)
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setSenderName(transaction.getSender().getName());
        response.setReceiverName(transaction.getReceiver().getName());
        response.setAmount(transaction.getAmount().toString());
        response.setTimestamp(transaction.getTimestamp());
        response.setNote(transaction.getNote());
        return response;
    }
}
