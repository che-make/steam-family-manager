package com.chema.steamfamilymanager.service.auth;

import com.chema.steamfamilymanager.dto.auth.AuthResponse;
import com.chema.steamfamilymanager.dto.auth.LoginRequest;
import com.chema.steamfamilymanager.jwt.JwtService;
import com.chema.steamfamilymanager.error.exception.NotFoundException;
import com.chema.steamfamilymanager.entities.user.User;
import com.chema.steamfamilymanager.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User: " + loginRequest.getEmail() + " not found"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .role(user.getRole().name())
                .build();
    }
}
