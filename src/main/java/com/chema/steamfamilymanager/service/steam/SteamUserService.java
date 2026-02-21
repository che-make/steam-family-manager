package com.chema.steamfamilymanager.service.steam;

import com.chema.steamfamilymanager.dto.steam.PlayerSummaryResponse;
import com.chema.steamfamilymanager.dto.steamuser.AddSteamUserRequest;
import com.chema.steamfamilymanager.dto.steamuser.GameResponse;
import com.chema.steamfamilymanager.dto.steamuser.SteamUserResponse;
import com.chema.steamfamilymanager.entities.steam.SteamUser;
import com.chema.steamfamilymanager.error.exception.DuplicateEntryException;
import com.chema.steamfamilymanager.error.exception.NotFoundException;
import com.chema.steamfamilymanager.repositories.steam.SteamUserRepository;
import com.chema.steamfamilymanager.repositories.steam.UserLibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SteamUserService {

    private final SteamUserRepository steamUserRepository;
    private final UserLibraryRepository userLibraryRepository;
    private final SteamApiService steamApiService;

    @Transactional
    public SteamUserResponse addFamilyMember(AddSteamUserRequest request) {
        if (steamUserRepository.existsById(request.getSteamId())) {
            throw new DuplicateEntryException("Steam user with ID " + request.getSteamId() + " already exists");
        }

        PlayerSummaryResponse.Player player = steamApiService.getPlayerSummary(request.getSteamId());

        SteamUser steamUser = new SteamUser();
        steamUser.setSteamId(request.getSteamId());
        steamUser.setPersonaName(player.getPersonaName());
        steamUser.setAvatarUrl(player.getAvatarFull());
        steamUser.setProfileUrl(player.getProfileUrl());
        steamUser.setActive(true);
        steamUser.setInitialSyncDone(false);

        steamUser = steamUserRepository.save(steamUser);
        return toResponse(steamUser);
    }

    public List<SteamUserResponse> getAllFamilyMembers() {
        return steamUserRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SteamUser> getActiveFamilyMembers() {
        return steamUserRepository.findByActiveTrue();
    }

    public SteamUserResponse getFamilyMember(String steamId) {
        SteamUser steamUser = findBySteamId(steamId);
        return toResponse(steamUser);
    }

    @Transactional
    public SteamUserResponse deactivateFamilyMember(String steamId) {
        SteamUser steamUser = findBySteamId(steamId);
        steamUser.setActive(false);
        steamUserRepository.save(steamUser);
        return toResponse(steamUser);
    }

    @Transactional
    public void deleteFamilyMember(String steamId) {
        SteamUser steamUser = findBySteamId(steamId);
        steamUserRepository.delete(steamUser);
    }

    public List<GameResponse> getFamilyMemberGames(String steamId) {
        findBySteamId(steamId);
        return userLibraryRepository.findBySteamUserSteamId(steamId).stream()
                .map(ul -> GameResponse.builder()
                        .appId(ul.getGame().getAppId())
                        .name(ul.getGame().getName())
                        .iconUrl(ul.getGame().getFullIconUrl())
                        .firstDetectedAt(ul.getFirstDetectedAt())
                        .build())
                .toList();
    }

    private SteamUser findBySteamId(String steamId) {
        return steamUserRepository.findById(steamId)
                .orElseThrow(() -> new NotFoundException("Steam user with ID " + steamId + " not found"));
    }

    private SteamUserResponse toResponse(SteamUser steamUser) {
        return SteamUserResponse.builder()
                .steamId(steamUser.getSteamId())
                .personaName(steamUser.getPersonaName())
                .avatarUrl(steamUser.getAvatarUrl())
                .profileUrl(steamUser.getProfileUrl())
                .active(steamUser.isActive())
                .gameCount(userLibraryRepository.countBySteamUserSteamId(steamUser.getSteamId()))
                .createdAt(steamUser.getCreatedAt())
                .build();
    }
}
