package com.example.fintech.service;

import com.example.fintech.exception.BadRequestException;
import com.example.fintech.model.PendingRequest;
import com.example.fintech.model.User;
import com.example.fintech.repository.PendingRequestRepository;
import com.example.fintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PendingRequestService {

    private final PendingRequestRepository pendingRequestRepository;
    private final UserRepository userRepository;

    public String createOtpRequest(Long userId, String receiver, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Generate fake OTP for testing
        String otp = String.format("%06d", new Random().nextInt(999999));

        PendingRequest request = new PendingRequest();
        request.setUser(user);
        request.setType("OTP_VERIFICATION");
        request.setReceiver(receiver);
        request.setAmount(amount);
        request.setOtp(otp);
        request.setStatus("PENDING");

        pendingRequestRepository.save(request);
        return otp;
    }

    public boolean verifyOtp(Long userId, String inputOtp) {
        Optional<PendingRequest> optionalRequest =
                pendingRequestRepository.findByUserIdAndTypeAndStatus(userId, "OTP_VERIFICATION", "PENDING");

        if (optionalRequest.isEmpty()) {
            throw new BadRequestException("No pending OTP request found");
        }

        PendingRequest request = optionalRequest.get();

        if (request.getOtp().equals(inputOtp)) {
            request.setStatus("VERIFIED");
            pendingRequestRepository.save(request);
            return true;
        } else {
            request.setStatus("FAILED");
            pendingRequestRepository.save(request);
            return false;
        }
    }
}
