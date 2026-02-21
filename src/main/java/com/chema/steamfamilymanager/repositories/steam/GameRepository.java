package com.chema.steamfamilymanager.repositories.steam;

import com.chema.steamfamilymanager.entities.steam.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
