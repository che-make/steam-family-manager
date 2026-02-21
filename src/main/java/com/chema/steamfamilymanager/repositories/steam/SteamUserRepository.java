package com.chema.steamfamilymanager.repositories.steam;

import com.chema.steamfamilymanager.entities.steam.SteamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SteamUserRepository extends JpaRepository<SteamUser, String> {
    List<SteamUser> findByActiveTrue();
}
