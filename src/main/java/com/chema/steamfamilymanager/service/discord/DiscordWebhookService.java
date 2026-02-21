package com.chema.steamfamilymanager.service.discord;

import com.chema.steamfamilymanager.entities.steam.Game;
import com.chema.steamfamilymanager.entities.steam.SteamUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordWebhookService {

    private final RestTemplate restTemplate;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    private final ConcurrentLinkedQueue<PendingNotification> notificationQueue = new ConcurrentLinkedQueue<>();

    private record PendingNotification(SteamUser steamUser, Game game, LocalDateTime detectedAt) {}

    public void sendNewGameNotification(SteamUser steamUser, Game game, LocalDateTime detectedAt) {
        notificationQueue.add(new PendingNotification(steamUser, game, detectedAt));
        log.info("Queued Discord notification for game '{}' (user '{}'). Queue size: {}",
                game.getName(), steamUser.getPersonaName(), notificationQueue.size());
    }

    @Scheduled(fixedDelay = 2000)
    public void processQueue() {
        PendingNotification notification = notificationQueue.peek();
        if (notification == null) {
            return;
        }

        try {
            sendToDiscord(notification);
            notificationQueue.poll();
            log.info("Discord notification sent for game '{}' (user '{}'). Remaining in queue: {}",
                    notification.game().getName(), notification.steamUser().getPersonaName(), notificationQueue.size());
        } catch (Exception e) {
            log.warn("Failed to send Discord notification for game '{}', will retry next cycle: {}",
                    notification.game().getName(), e.getMessage());
        }
    }

    private void sendToDiscord(PendingNotification notification) {
        SteamUser steamUser = notification.steamUser();
        Game game = notification.game();
        LocalDateTime detectedAt = notification.detectedAt();

        String storeUrl = "https://store.steampowered.com/app/" + game.getAppId();
        String iconUrl = game.getFullIconUrl();

        Map<String, Object> embed = Map.of(
                "title", game.getName(),
                "url", storeUrl,
                "color", 0x1b2838,
                "thumbnail", iconUrl != null ? Map.of("url", iconUrl) : Map.of(),
                "fields", List.of(
                        Map.of("name", "Player", "value", steamUser.getPersonaName(), "inline", true),
                        Map.of("name", "Detected at", "value", detectedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "inline", true),
                        Map.of("name", "Profile", "value", "[Steam Profile](" + steamUser.getProfileUrl() + ")", "inline", false)
                ),
                "footer", Map.of("text", "Steam Family Manager")
        );

        Map<String, Object> payload = Map.of(
                "embeds", List.of(embed)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(webhookUrl, request, String.class);
    }
}
