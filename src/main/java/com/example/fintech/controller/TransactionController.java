package com.example.fintech.controller;


import com.example.fintech.dto.*;
import com.example.fintech.dto.payload.OtpPayload;
import com.example.fintech.model.BankAccount;
import com.example.fintech.model.Transaction;
import com.example.fintech.model.User;
import com.example.fintech.repository.BankAccountRepository;
import com.example.fintech.repository.UserRepository;
import com.example.fintech.service.BankAccountService;
import com.example.fintech.service.TransactionOtpService;
import com.example.fintech.service.TransactionService;
import com.example.fintech.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionOtpService transactionOtpService;






    @PostMapping("/complete-transfer")
    public ResponseEntity<ApiResponse<?>> completeTransfer(@RequestBody CompleteTransferDTO request) {
        User user = userService.getLoggedInUser();
        Long senderUserId = user.getId(); // get from token in real app

        log.info("Received OTP request: {}", request);
        try {
            boolean success = transactionOtpService.completeTransfer(senderUserId, request);
            if (success) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .success(true)
                        .message("Transaction completed successfully")
                        .status(200)
                        .build());
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .success(false)
                        .message("Invalid OTP or transaction failed")
                        .status(400)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .success(false)
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }


    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<?>> requestOtp(
            @RequestBody RequestOtpDTO request,
            Principal principal
    ) {
        log.info("Request Otp DTO request: {}", request);
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Amount is required and must be greater than zero")
                            .status(400)
                            .build()
            );
        }


        // ✅ Principal holds userId because filter put it there
        User user = userService.getLoggedInUser();
        Long userId = user.getId(); // get from token in real app
        String otp = transactionOtpService.generateAndSaveOtp(
                userId,
                request.getReceiverInput(),
                request.getTransferType(),
                request.getBankName(),
                request.getAmount()
        );

        ApiResponse<OtpPayload> response = ApiResponse.<OtpPayload>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data(new OtpPayload(otp))
                .message("OTP generated successfully")
                .status(200)
                .build();

        return ResponseEntity.ok(response);
    }



    @PostMapping("/verify-receiver")
    public ResponseEntity<ApiResponse<?>> verifyReceiver(@RequestBody VerifyReceiverDTO request) {
        log.info("VerifyReceiver request: {}", request);

        // Self-transfer check - agar logged in user khud ko paise bhej raha hai
        User loggedInUser = userService.getLoggedInUser();

        if ("FINTECH".equalsIgnoreCase(request.getType())) {
            // Check if user is trying to send money to himself
            if (loggedInUser.getMobileNumber().equals(request.getMobileNumber())) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .success(false)
                                .message("You cannot send money to yourself")
                                .status(400)
                                .build()
                );
            }

            Optional<User> userOpt = userService.findByMobileNumber(request.getMobileNumber());

            if (userOpt.isPresent()) {
                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .success(true)
                                .data(userOpt.get())
                                .message("Fintech user found")
                                .status(200)
                                .build()
                );
            } else {
                return ResponseEntity.status(404).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("User not found with this mobile number. Please check the number and try again.")
                                .status(404)
                                .build()
                );
            }

        } else if ("BANKS".equalsIgnoreCase(request.getType())) {
            log.info("request type: {}", request.getType());

            Optional<BankAccount> result = bankAccountService.findByAccount(request.getAccount());
            log.info("result: {}", result);
            if (result.isPresent()) {
                ReceiverAccountDTO dto = new ReceiverAccountDTO(
                        result.get().getBankName(),
                        result.get().getAccountNumber(),
                        result.get().getAccountHolderName()
                );

                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .success(true)
                                .data(dto)
                                .message("Bank account found")
                                .status(200)
                                .build()
                );
            } else {
                return ResponseEntity.status(404).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Bank account not found. Please verify the account number and try again.")
                                .status(404)
                                .build()
                );
            }

        } else if ("OTHERWALLET".equalsIgnoreCase(request.getType())) {
            log.info("OTHERWALLET request type: {}", request.getType());
            log.info("request getMobileNumber: {}", request.getMobileNumber());

            Optional<BankAccount> result = bankAccountService.findByAccount(request.getMobileNumber());
            log.info("result: {}", result);
            if (result.isPresent()) {
                ReceiverAccountDTO dto = new ReceiverAccountDTO(
                        result.get().getBankName(),
                        result.get().getAccountNumber(),
                        result.get().getAccountHolderName()
                );

                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .success(true)
                                .data(dto)
                                .message("Bank account found")
                                .status(200)
                                .build()
                );
            } else {
                return ResponseEntity.status(404).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Bank account not found. Please verify the account number and try again.")
                                .status(404)
                                .build()
                );
            }

        }else {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Invalid transfer type. Please select either Fintech or Bank transfer.")
                            .status(400)
                            .build()
            );
        }
    }



}
