package com.chema.steamfamilymanager.service.steam;

import com.chema.steamfamilymanager.dto.steam.OwnedGamesResponse;
import com.chema.steamfamilymanager.dto.steam.PlayerSummaryResponse;
import com.chema.steamfamilymanager.error.exception.SteamApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SteamApiService {

    private final RestTemplate restTemplate;

    @Value("${steam.api.key}")
    private String apiKey;

    @Value("${steam.api.base-url}")
    private String baseUrl;

    public OwnedGamesResponse getOwnedGames(String steamId) {
        String url = baseUrl + "/IPlayerService/GetOwnedGames/v1/?key=" + apiKey
                + "&steamid=" + steamId
                + "&include_appinfo=true&include_played_free_games=true&include_free_sub=true&skip_unvetted_apps=false&format=json";

        try {
            OwnedGamesResponse response = restTemplate.getForObject(url, OwnedGamesResponse.class);
            if (response == null || response.getResponse() == null) {
                throw new SteamApiException("Empty response from Steam API for steamId: " + steamId);
            }
            return response;
        } catch (RestClientException e) {
            throw new SteamApiException("Failed to fetch owned games for steamId: " + steamId, e);
        }
    }

    public PlayerSummaryResponse.Player getPlayerSummary(String steamId) {
        String url = baseUrl + "/ISteamUser/GetPlayerSummaries/v2/?key=" + apiKey
                + "&steamids=" + steamId + "&format=json";

        try {
            PlayerSummaryResponse response = restTemplate.getForObject(url, PlayerSummaryResponse.class);
            if (response == null || response.getResponse() == null
                    || response.getResponse().getPlayers() == null
                    || response.getResponse().getPlayers().isEmpty()) {
                throw new SteamApiException("Player not found on Steam for steamId: " + steamId);
            }
            return response.getResponse().getPlayers().get(0);
        } catch (RestClientException e) {
            throw new SteamApiException("Failed to fetch player summary for steamId: " + steamId, e);
        }
    }
}
