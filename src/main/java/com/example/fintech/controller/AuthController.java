package com.example.fintech.controller;

import com.example.fintech.dto.ApiResponse;
import com.example.fintech.dto.AuthResponse;
import com.example.fintech.dto.LoginRequest;
import com.example.fintech.dto.SignUpRequest;
import com.example.fintech.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .timestamp(LocalDateTime.now())
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }


//    @PostMapping("/signup")
//    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
//        return ResponseEntity.ok(authService.registerUser(signUpRequest));
//    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        // Usually stateless JWT: no server action needed
        // If token blacklist implemented, add token to blacklist here
        return ResponseEntity.ok().body("User signed out successfully");
    }
}