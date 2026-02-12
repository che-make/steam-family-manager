package com.metamedicsvr.springtemplate.dto.auth;

import com.metamedicsvr.springtemplate.dto.auth.register.RegisterRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RegisterTeacherRequest extends RegisterRequest {

    @NotBlank(message = "Organization Id is required")
    private UUID organizationId;

}
