package com.example.fintech.controller;

import com.example.fintech.dto.*;
import com.example.fintech.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Map<String, String>>> sendOtp(@Valid @RequestBody SendOtp sendOtp) {
        String otp = verificationService.sendOtp(sendOtp.getMobileNumber(), sendOtp.getCountry());

        Map<String, String> data = Map.of("otp", otp);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message("OTP sent successfully (for demo/testing purposes)")
                .data(data)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        verificationService.verifyOtp(request.getMobileNumber(), request.getOtp());

        return ResponseEntity.ok(ApiResponse.<Object>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message("OTP verified and user created successfully.")
                .status(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<ApiResponse<AuthResponse>> completeRegistration(
            @Valid @RequestBody CompleteRegistrationRequest request) {

        AuthResponse response = verificationService.completeRegistration(request);

        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message("Registration completed and logged in successfully.")
                .data(response)
                .status(HttpStatus.OK.value())
                .build());
    }



}
