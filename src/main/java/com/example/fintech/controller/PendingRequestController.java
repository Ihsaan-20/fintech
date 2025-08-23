package com.example.fintech.controller;


import com.example.fintech.dto.ApiResponse;
import com.example.fintech.dto.TransactionOtpDTO;
import com.example.fintech.model.PendingRequest;
import com.example.fintech.dto.VerifyOtpRequest;
import com.example.fintech.service.PendingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class PendingRequestController {

    private final PendingRequestService pendingRequestService;

    @PostMapping("/create-otp-request")
    public ResponseEntity<ApiResponse<String>> createOtpRequest(@RequestBody TransactionOtpDTO request, HttpServletRequest httpRequest) {
        String otp = pendingRequestService.createOtpRequest(request.getUserId(), request.getReceiverMobileNumber(), request.getAmount());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data("Generated OTP: " + otp)
                .message("OTP generated successfully")
                .status(HttpStatus.OK.value())
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody VerifyOtpRequest request, HttpServletRequest httpRequest) {
        boolean verified = pendingRequestService.verifyOtp(request.getUserId(), request.getOtp());

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .timestamp(LocalDateTime.now())
                .success(verified)
                .data(verified)
                .message(verified ? "OTP Verified ✅" : "OTP Invalid ❌")
                .status(verified ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.status(verified ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    // Future enhancements (T-PIN, Bank Transfer) can follow the same pattern
}


