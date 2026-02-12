package com.metamedicsvr.springtemplate.dto.auth;

import com.metamedicsvr.springtemplate.dto.auth.register.RegisterRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterStudentRequest extends RegisterRequest {

    @NotBlank(message = "School class code is required")
    private String schoolClassCode;

}
