package com.example.fintech.controller;

import com.example.fintech.dto.*;
import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import com.example.fintech.security.CurrentUser;
import com.example.fintech.security.UserPrincipal;
import com.example.fintech.service.TransactionService;
import com.example.fintech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse<?>> getCurrentMonthTransactions() {
        User user = userService.getLoggedInUser();
        log.info("current-month user: {}", user);

        TransactionResponseDto transactions = transactionService.getCurrentMonthTransactions(user);


        return ResponseEntity.ok(ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data(transactions)
                .message("Transactions fetched successfully")
                .status(200)
                .build());
    }




    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getUserBalance() {
        try {
            User user = userService.getLoggedInUser();
            BigDecimal currentBalance = userService.getUserCurrentBalance(user.getMobileNumber());

            Map<String, BigDecimal> balanceData = Map.of("balance", currentBalance);

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, BigDecimal>>builder()
                            .success(true)
                            .data(balanceData)
                            .message("Balance retrieved successfully")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Map<String, BigDecimal>>builder()
                            .success(false)
                            .message("Failed to retrieve balance")
                            .build()
                    );
        }
    }


}
