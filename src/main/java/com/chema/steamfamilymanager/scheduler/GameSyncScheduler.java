package com.chema.steamfamilymanager.scheduler;

import com.chema.steamfamilymanager.dto.steam.OwnedGamesResponse;
import com.chema.steamfamilymanager.entities.steam.Game;
import com.chema.steamfamilymanager.entities.steam.SteamUser;
import com.chema.steamfamilymanager.entities.steam.UserLibrary;
import com.chema.steamfamilymanager.repositories.steam.SteamUserRepository;
import com.chema.steamfamilymanager.repositories.steam.UserLibraryRepository;
import com.chema.steamfamilymanager.service.discord.DiscordWebhookService;
import com.chema.steamfamilymanager.service.steam.GameService;
import com.chema.steamfamilymanager.service.steam.SteamApiService;
import com.chema.steamfamilymanager.service.steam.SteamUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameSyncScheduler {

    private final SteamUserService steamUserService;
    private final SteamApiService steamApiService;
    private final GameService gameService;
    private final DiscordWebhookService discordWebhookService;
    private final UserLibraryRepository userLibraryRepository;
    private final SteamUserRepository steamUserRepository;

    @Scheduled(fixedRateString = "${game.sync.fixed-rate}")
    public void syncGames() {
        List<SteamUser> activeUsers = steamUserService.getActiveFamilyMembers();
        log.info("Starting game sync for {} active users", activeUsers.size());

        for (SteamUser steamUser : activeUsers) {
            try {
                syncUserGames(steamUser);
            } catch (Exception e) {
                log.error("Error syncing games for user '{}' ({}): {}",
                        steamUser.getPersonaName(), steamUser.getSteamId(), e.getMessage());
            }
        }

        log.info("Game sync completed");
    }

    public void syncSingleUser(String steamId) {
        SteamUser steamUser = steamUserService.getActiveFamilyMembers().stream()
                .filter(u -> u.getSteamId().equals(steamId))
                .findFirst()
                .orElseThrow(() -> new com.chema.steamfamilymanager.error.exception.NotFoundException(
                        "Active steam user with ID " + steamId + " not found"));
        syncUserGames(steamUser);
    }

    public void syncUserGames(SteamUser steamUser) {
        OwnedGamesResponse ownedGames = steamApiService.getOwnedGames(steamUser.getSteamId());

        if (ownedGames.getResponse().getGames() == null) {
            log.warn("No games found for user '{}' ({})", steamUser.getPersonaName(), steamUser.getSteamId());
            return;
        }

        Set<Integer> existingAppIds = userLibraryRepository.findAppIdsBySteamId(steamUser.getSteamId());
        boolean isInitialSync = !steamUser.isInitialSyncDone();
        LocalDateTime now = LocalDateTime.now();

        for (OwnedGamesResponse.GameInfo gameInfo : ownedGames.getResponse().getGames()) {
            if (!existingAppIds.contains(gameInfo.getAppId())) {
                Game game = gameService.getOrCreateGame(gameInfo.getAppId(), gameInfo.getName(), gameInfo.getImgIconUrl());

                UserLibrary entry = new UserLibrary();
                entry.setSteamUser(steamUser);
                entry.setGame(game);
                entry.setFirstDetectedAt(now);
                userLibraryRepository.save(entry);

                if (!isInitialSync) {
                    discordWebhookService.sendNewGameNotification(steamUser, game, now);
                }
            }
        }

        if (isInitialSync) {
            steamUser.setInitialSyncDone(true);
            steamUserRepository.save(steamUser);
            log.info("Initial sync completed for user '{}' ({}) - {} games imported",
                    steamUser.getPersonaName(), steamUser.getSteamId(), ownedGames.getResponse().getGames().size());
        }
    }
}
