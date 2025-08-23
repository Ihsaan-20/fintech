package com.example.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private boolean success;
    private T data; // Payload if success
    private String message; // Success or error message
    private Integer status; // HTTP status code
    private String path; // Request path, optional

    // For errors, you can reuse this same structure with success = false
}
