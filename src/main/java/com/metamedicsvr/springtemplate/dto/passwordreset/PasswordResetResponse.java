package com.metamedicsvr.springtemplate.dto.passwordreset;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponse {
    @NotBlank(message = "Token is required")
    private String passwordResetToken;
}
