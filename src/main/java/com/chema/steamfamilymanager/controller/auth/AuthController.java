package com.chema.steamfamilymanager.controller.auth;

import com.chema.steamfamilymanager.dto.auth.AuthResponse;
import com.chema.steamfamilymanager.dto.auth.LoginRequest;
import com.chema.steamfamilymanager.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login",
            description = "Returns a JWT token and the role of the user"
    )
    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(
            summary = "Validate a JWT token",
            description = "Returns 200 if the token is valid, otherwise 401",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/validateToken")
    public ResponseEntity<Void> validateToken() {
        return ResponseEntity.ok().build();
    }
}
