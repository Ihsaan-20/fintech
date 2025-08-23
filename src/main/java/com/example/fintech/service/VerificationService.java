package com.example.fintech.service;

import com.example.fintech.dto.AuthResponse;
import com.example.fintech.dto.CompleteRegistrationRequest;
import com.example.fintech.dto.UserResponse;
import com.example.fintech.exception.BadRequestException;
import com.example.fintech.model.Card;
import com.example.fintech.model.User;
import com.example.fintech.model.VerificationCode;
import com.example.fintech.repository.UserRepository;
import com.example.fintech.repository.VerificationCodeRepository;
import com.example.fintech.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_SECONDS = 60; // 1 minute

    private final SecureRandom random = new SecureRandom();

    // Store OTP against mobileNumber with expiry time
    private final Map<String, OtpEntry> otpCache = new ConcurrentHashMap<>();

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UtilityService utilityService;

    @Transactional
    public AuthResponse completeRegistration(CompleteRegistrationRequest request) {
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElseThrow(() -> new BadRequestException("User not found with mobile number"));

        // Update user details
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String username = request.getFirstName().toLowerCase() + request.getLastName().toLowerCase() + "@dia.com";
        user.setUsername(username);
        user.setBalance(new BigDecimal("1500.00"));

        // Create and set Card details (example hardcoded, replace with real data)
//        Card card = new Card();
//        card.setCardNumber(utilityService.generateRandomCardNumber());
//        card.setExpiryDate("12/25");
//        card.setCvv(passwordEncoder.encode("100"));
//        card.setCardHolderName(user.getFirstName() + " " + user.getLastName());
//        card.setUser(user);
//
//        user.setCard(card);
        userRepository.save(user);



        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getMobileNumber(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        // Create UserResponse from User entity
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getMobileNumber(),
                user.getFirstName() + (user.getMiddleName().isEmpty() ? "" : " " + user.getMiddleName()) + " " + user.getLastName(),
                user.getBalance()
        );

        return new AuthResponse(token, userResponse);
    }



    private void saveSendOtpRecord(String mobileNumber, String countryCode, String otpCode, int sendCodeCount) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setMobileNumber(mobileNumber);
        verificationCode.setCountryCode(countryCode);
        verificationCode.setCode(otpCode);
        verificationCode.setIsVerified(false);
        verificationCode.setSendCodeCount(sendCodeCount);
        verificationCode.setCodeFor("registration");

        verificationCodeRepository.save(verificationCode);
    }

    public String sendOtp(String mobileNumber, String country) {
        String otp = generateOtp();

        // 1. Today range
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        long todayCount = verificationCodeRepository
                .countByMobileNumberAndCreatedAtBetween(mobileNumber, todayStart, todayEnd);

        if (todayCount >= 3) {
            throw new BadRequestException("OTP limit exceeded. You can send maximum 3 OTPs per day.");
        }

        VerificationCode latest = verificationCodeRepository
                .findTopByMobileNumberOrderByCreatedAtDesc(mobileNumber)
                .orElse(null);

        if (latest != null && Boolean.TRUE.equals(latest.getIsVerified())) {
            throw new BadRequestException("This mobile number is already registered and verified.");
        }

        int sendCount = 1;
        if (latest != null) {
            sendCount = latest.getSendCodeCount() + 1;
        }

        saveSendOtpRecord(mobileNumber, country, otp, sendCount);

        OtpEntry otpEntry = new OtpEntry(otp, LocalDateTime.now().plusSeconds(OTP_VALIDITY_SECONDS));
        otpCache.put(mobileNumber, otpEntry);

        return otp; // test/demo
    }





    @Transactional
    public void verifyOtp(String mobileNumber, String otp) {
        VerificationCode code = verificationCodeRepository
                .findTopByMobileNumberOrderByCreatedAtDesc(mobileNumber)
                .orElseThrow(() -> new BadRequestException("Invalid OTP or number"));

        if (code.getIsVerified()) {
            throw new BadRequestException("This number is already verified.");
        }

        if (!code.getCode().equals(otp)) {
            throw new BadRequestException("Invalid OTP or number");
        }

        if (code.getCreatedAt().plusSeconds(OTP_VALIDITY_SECONDS).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired. Please request a new one.");
        }

        // Mark verified
        code.setIsVerified(true);
        code.setUpdatedAt(LocalDateTime.now());
        verificationCodeRepository.save(code);

        // Create user only if not exists
        userRepository.findByMobileNumber(mobileNumber)
                .orElseGet(() -> {
                    User user = new User();
                    user.setMobileNumber(mobileNumber);
                    user.setPassword(""); // empty or generate temp password
                    return userRepository.save(user);
                });
    }

    private String generateOtp() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }


}
