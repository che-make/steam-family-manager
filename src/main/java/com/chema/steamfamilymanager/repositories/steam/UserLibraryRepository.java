package com.chema.steamfamilymanager.repositories.steam;

import com.chema.steamfamilymanager.entities.steam.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

    @Query("SELECT ul.game.appId FROM UserLibrary ul WHERE ul.steamUser.steamId = :steamId")
    Set<Integer> findAppIdsBySteamId(@Param("steamId") String steamId);

    List<UserLibrary> findBySteamUserSteamId(String steamId);

    long countBySteamUserSteamId(String steamId);
}
