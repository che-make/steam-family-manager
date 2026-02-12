package com.metamedicsvr.springtemplate.controller.passwordreset;

import com.metamedicsvr.springtemplate.dto.passwordreset.ForgotPasswordRequest;
import com.metamedicsvr.springtemplate.dto.passwordreset.OTPRequest;
import com.metamedicsvr.springtemplate.dto.passwordreset.PasswordResetRequest;
import com.metamedicsvr.springtemplate.dto.passwordreset.PasswordResetResponse;
import com.metamedicsvr.springtemplate.enums.Language;
import com.metamedicsvr.springtemplate.service.passwordreset.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("password")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Operations related to password reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(
            summary = "Send password reset email",
            description = "Send a password reset email to the user, if the email is valid"
    )
    @PermitAll
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(
            @RequestParam(defaultValue = "EN") String lang,
            @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) throws MessagingException {
        Language language = Language.fromInitial(lang);
        passwordResetService.sendOtp(forgotPasswordRequest.getEmail(), language);

        return ResponseEntity.ok("Recuperation mail sent to " + forgotPasswordRequest.getEmail());
    }

    @Operation(
            summary = "Receive OTP for password reset",
            description = "Receive OTP for password reset"
    )
    @PermitAll
    @PostMapping("/receive-otp")
    public ResponseEntity<PasswordResetResponse> receiveOTP(@RequestBody @Valid OTPRequest otpRequest) {
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse(
                passwordResetService.receiveOtp(otpRequest.getEmail(), otpRequest.getOtpCode())
        );

        return ResponseEntity.ok(passwordResetResponse);
    }

    @Operation(
            summary = "Reset password",
            description = "Reset the password of the user, needs a valid token asocited with the user"
    )
    @PermitAll
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.resetPasswordRequest(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

}
