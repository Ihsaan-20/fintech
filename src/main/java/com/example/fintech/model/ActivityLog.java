package com.example.fintech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber;

    private String activity; // OTP sent, OTP verified, User created, etc.

    private String ipAddress; // ✅ New field to store IP address

    private LocalDateTime timestamp = LocalDateTime.now();

    public ActivityLog(String mobileNumber, String activity, String ipAddress) {
        this.mobileNumber = mobileNumber;
        this.activity = activity;
        this.ipAddress = ipAddress;
    }
}
