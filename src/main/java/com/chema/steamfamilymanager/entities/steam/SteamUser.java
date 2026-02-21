package com.chema.steamfamilymanager.entities.steam;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "steam_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SteamUser {

    @Id
    @Column(length = 17)
    private String steamId;

    private String personaName;

    private String avatarUrl;

    private String profileUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean initialSyncDone = false;

    @OneToMany(mappedBy = "steamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLibrary> libraryEntries = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
