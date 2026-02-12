package com.metamedicsvr.springtemplate.dto.passwordreset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OTPRequest {

    @NotBlank(message = "OTP code is required")
    private String otpCode;

    @NotBlank(message = "Email is required")
    @Email(message = "Email hasn't a valid format")
    private String email;

}
