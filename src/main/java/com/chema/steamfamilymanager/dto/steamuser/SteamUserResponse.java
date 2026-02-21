package com.chema.steamfamilymanager.dto.steamuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SteamUserResponse {
    private String steamId;
    private String personaName;
    private String avatarUrl;
    private String profileUrl;
    private boolean active;
    private long gameCount;
    private LocalDateTime createdAt;
}
