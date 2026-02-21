package com.chema.steamfamilymanager.service.steam;

import com.chema.steamfamilymanager.entities.steam.Game;
import com.chema.steamfamilymanager.repositories.steam.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game getOrCreateGame(int appId, String name, String imgIconUrl) {
        return gameRepository.findById(appId).orElseGet(() -> {
            Game game = new Game();
            game.setAppId(appId);
            game.setName(name);
            game.setImgIconUrl(imgIconUrl);
            return gameRepository.save(game);
        });
    }
}
