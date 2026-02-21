package com.chema.steamfamilymanager.entities.steam;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_library", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"steam_id", "app_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "steam_id", nullable = false)
    private SteamUser steamUser;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private LocalDateTime firstDetectedAt;
}
