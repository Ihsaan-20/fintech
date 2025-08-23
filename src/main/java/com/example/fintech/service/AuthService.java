package com.example.fintech.service;

import com.example.fintech.dto.AuthResponse;
import com.example.fintech.dto.LoginRequest;
import com.example.fintech.dto.SignUpRequest;
import com.example.fintech.dto.UserResponse;
import com.example.fintech.model.User;
import com.example.fintech.security.JwtTokenProvider;
import com.example.fintech.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        // Use mobileNumber instead of email
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getMobileNumber(),  // changed here
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponse userResponse = new UserResponse(userPrincipal.getId(), userPrincipal.getMobileNumber(), userPrincipal.getName(), userPrincipal.getBalance());
        String jwt = tokenProvider.generateToken(authentication);
        return new AuthResponse(jwt, userResponse);
    }

    public AuthResponse registerUser(SignUpRequest signUpRequest) {
        User user = userService.createUser(signUpRequest);

        // Auto login after registration
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber(signUpRequest.getMobileNumber());  // changed here
        loginRequest.setPassword(signUpRequest.getPassword());

        return authenticateUser(loginRequest);
    }
}
