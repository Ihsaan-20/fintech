package com.example.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private long count;
    private String currentMonthWithYear;
    private List<TransactionDto> transactions;
}
