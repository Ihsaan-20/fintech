package com.example.fintech.service;

import com.example.fintech.dto.ReceiverAccountDTO;
import com.example.fintech.dto.SignUpRequest;
import com.example.fintech.exception.BadRequestException;
import com.example.fintech.model.BankAccount;
import com.example.fintech.model.User;
import com.example.fintech.repository.BankAccountRepository;
import com.example.fintech.repository.UserRepository;
import com.example.fintech.security.CustomUserDetailsService;
import com.example.fintech.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankAccountRepository bankAccountRepository;

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found in DB"));
    }


    @Transactional
    public User createUser(SignUpRequest signUpRequest) {
        // Check if mobile number already exists
        if (userRepository.existsByMobileNumber(signUpRequest.getMobileNumber())) {
            throw new BadRequestException("Mobile number already in use.");
        }

        User user = new User();
        user.setMobileNumber(signUpRequest.getMobileNumber());

        // Password may be null or empty during initial signup, set if present
        if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }

        // Initially no firstName, lastName, username (user will update later)
        user.setBalance(BigDecimal.valueOf(1000)); // Starting balance (optional)

        // Username generation: if firstName and lastName present, else can leave null for now
        if (signUpRequest.getFirstName() != null && signUpRequest.getLastName() != null) {
            String username = signUpRequest.getFirstName() + signUpRequest.getLastName() + "@dia.com";
            user.setUsername(username);
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setMiddleName(signUpRequest.getMiddleName());
        }

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public Optional<ReceiverAccountDTO> findFintechReceiverAsBankAccount(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(user -> new ReceiverAccountDTO(
                        "FINTECH",
                        user.getMobileNumber(),
                        user.getFirstName() + " " + user.getLastName()
                ));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public BigDecimal getUserCurrentBalance(String input) {
        return userRepository.findByMobileNumber(input)
                .map(user -> user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO)
                .orElseThrow(() -> new BadRequestException("User not found!"));
    }




}
