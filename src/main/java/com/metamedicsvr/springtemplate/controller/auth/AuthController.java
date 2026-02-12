package com.metamedicsvr.springtemplate.controller.auth;

import com.metamedicsvr.springtemplate.dto.auth.AuthResponse;
import com.metamedicsvr.springtemplate.dto.auth.LoginRequest;
import com.metamedicsvr.springtemplate.dto.auth.RegisterStudentRequest;
import com.metamedicsvr.springtemplate.dto.auth.RegisterTeacherRequest;
import com.metamedicsvr.springtemplate.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
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
@Tag(name = "Auth", description = "Operations related to authentication and registration")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login a user",
            description = "Returns a JWT token and the role of the user if is found in the database"
    )
    @PermitAll
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(
            summary = "Register a teacher using an institutionId",
            description = "Returns a JWT token if the teacher is successfully registered, needs a valid institutionId",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "register/teacher")
    public ResponseEntity<AuthResponse> registerTeacher(@Valid @RequestBody RegisterTeacherRequest registerTeacherRequest) {
        return ResponseEntity.ok(authService.registerTeacher(registerTeacherRequest));
    }

    @Operation(
            summary = "Register a student using a classroomId",
            description = "Returns a JWT token if the student is successfully registered, needs a valid institutionId and a classroomId"
    )
    @PermitAll
    @PostMapping(value = "register/student")
    public ResponseEntity<AuthResponse> registerStudent(@Valid @RequestBody RegisterStudentRequest registerStudentRequest) {
        return ResponseEntity.ok(authService.registerStudent(registerStudentRequest));
    }

    @Operation(
            summary = "Validate a JWT token",
            description = "Returns a 200 status code if the token is valid, otherwise a 401 status code",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/validateToken")
    public ResponseEntity<Void> validateToken() {
        // No need to do anything, the security filter will take care of the validation
        return ResponseEntity.ok().build();
    }

}
