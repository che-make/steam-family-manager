package com.chema.steamfamilymanager.controller.steam;

import com.chema.steamfamilymanager.dto.steamuser.AddSteamUserRequest;
import com.chema.steamfamilymanager.dto.steamuser.GameResponse;
import com.chema.steamfamilymanager.dto.steamuser.SteamUserResponse;
import com.chema.steamfamilymanager.dto.sync.SyncResponse;
import com.chema.steamfamilymanager.scheduler.GameSyncScheduler;
import com.chema.steamfamilymanager.service.steam.SteamUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/family-members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Family Members", description = "CRUD operations for Steam family members")
@SecurityRequirement(name = "bearerAuth")
public class SteamUserController {

    private final SteamUserService steamUserService;
    private final GameSyncScheduler gameSyncScheduler;

    @Operation(summary = "Add a family member", description = "Adds a new Steam user to track by their Steam64 ID")
    @PostMapping
    public ResponseEntity<SteamUserResponse> addFamilyMember(@Valid @RequestBody AddSteamUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(steamUserService.addFamilyMember(request));
    }

    @Operation(summary = "List all family members", description = "Returns all tracked Steam family members")
    @GetMapping
    public ResponseEntity<List<SteamUserResponse>> getAllFamilyMembers() {
        return ResponseEntity.ok(steamUserService.getAllFamilyMembers());
    }

    @Operation(summary = "Get a family member", description = "Returns a specific Steam family member by Steam ID")
    @GetMapping("/{steamId}")
    public ResponseEntity<SteamUserResponse> getFamilyMember(@PathVariable String steamId) {
        return ResponseEntity.ok(steamUserService.getFamilyMember(steamId));
    }

    @Operation(summary = "Deactivate a family member", description = "Soft-deletes a family member (stops tracking)")
    @PatchMapping("/{steamId}/deactivate")
    public ResponseEntity<SteamUserResponse> deactivateFamilyMember(@PathVariable String steamId) {
        return ResponseEntity.ok(steamUserService.deactivateFamilyMember(steamId));
    }

    @Operation(summary = "Delete a family member", description = "Hard-deletes a family member and all their library entries")
    @DeleteMapping("/{steamId}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable String steamId) {
        steamUserService.deleteFamilyMember(steamId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get games of a family member", description = "Returns all games in a family member's library")
    @GetMapping("/{steamId}/games")
    public ResponseEntity<List<GameResponse>> getFamilyMemberGames(@PathVariable String steamId) {
        return ResponseEntity.ok(steamUserService.getFamilyMemberGames(steamId));
    }

    @Operation(summary = "Sync all members", description = "Triggers a manual game sync for all active family members")
    @PostMapping("/sync")
    public ResponseEntity<SyncResponse> syncAllMembers() {
        gameSyncScheduler.syncGames();
        return ResponseEntity.ok(new SyncResponse("Game sync completed for all active members"));
    }

    @Operation(summary = "Sync a single member", description = "Triggers a manual game sync for a specific family member")
    @PostMapping("/{steamId}/sync")
    public ResponseEntity<SyncResponse> syncMember(@PathVariable String steamId) {
        gameSyncScheduler.syncSingleUser(steamId);
        return ResponseEntity.ok(new SyncResponse("Game sync completed for member " + steamId));
    }
}
