package com.example.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String mobileNumber;
    private String name;
    private BigDecimal balance;
}
