package com.example.fintech.controller;

import com.example.fintech.dto.TransactionRequest;
import com.example.fintech.dto.TransactionResponse;
import com.example.fintech.model.User;
import com.example.fintech.security.CurrentUser;
import com.example.fintech.security.UserPrincipal;
import com.example.fintech.service.TransactionService;
import com.example.fintech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/send")
    public ResponseEntity<TransactionResponse> sendMoney(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.sendMoney(userPrincipal.getId(), transactionRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(@CurrentUser UserPrincipal userPrincipal) {
        List<TransactionResponse> transactions = transactionService.getUserTransactions(userPrincipal.getId());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user.getBalance());
    }
}
