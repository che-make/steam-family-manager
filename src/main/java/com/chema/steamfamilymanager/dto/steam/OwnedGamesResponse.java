package com.chema.steamfamilymanager.dto.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnedGamesResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("game_count")
        private int gameCount;

        @JsonProperty("games")
        private List<GameInfo> games;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameInfo {
        @JsonProperty("appid")
        private int appId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("img_icon_url")
        private String imgIconUrl;
    }
}
