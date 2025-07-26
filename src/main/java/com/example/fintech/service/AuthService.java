package com.example.fintech.service;

import com.example.fintech.dto.AuthResponse;
import com.example.fintech.dto.LoginRequest;
import com.example.fintech.dto.SignUpRequest;
import com.example.fintech.exception.BadRequestException;
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = tokenProvider.generateToken(authentication);
        return new AuthResponse(jwt, userPrincipal.getId(), userPrincipal.getEmail(), userPrincipal.getName());
    }

    public AuthResponse registerUser(SignUpRequest signUpRequest) {
        User user = userService.createUser(signUpRequest);

        // Auto login after registration
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(signUpRequest.getEmail());
        loginRequest.setPassword(signUpRequest.getPassword());

        return authenticateUser(loginRequest);
    }
}