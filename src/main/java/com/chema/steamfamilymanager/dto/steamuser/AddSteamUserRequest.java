package com.chema.steamfamilymanager.dto.steamuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSteamUserRequest {

    @NotBlank(message = "Steam ID is required")
    @Pattern(regexp = "\\d{17}", message = "Steam ID must be exactly 17 digits")
    private String steamId;
}
