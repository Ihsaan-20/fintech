package com.example.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphPoint {
    private long ts;
    private int upload;   // Upload Speed (Mbps)
    private int download; // Download Speed (Mbps)
}
