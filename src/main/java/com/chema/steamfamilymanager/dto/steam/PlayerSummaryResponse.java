package com.chema.steamfamilymanager.dto.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerSummaryResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("players")
        private List<Player> players;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        @JsonProperty("steamid")
        private String steamId;

        @JsonProperty("personaname")
        private String personaName;

        @JsonProperty("avatarfull")
        private String avatarFull;

        @JsonProperty("profileurl")
        private String profileUrl;
    }
}
