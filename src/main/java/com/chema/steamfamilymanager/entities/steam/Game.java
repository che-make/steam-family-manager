package com.chema.steamfamilymanager.entities.steam;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    private Integer appId;

    @Column(nullable = false)
    private String name;

    private String imgIconUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getFullIconUrl() {
        if (imgIconUrl == null || imgIconUrl.isBlank()) {
            return null;
        }
        return "https://media.steampowered.com/steamcommunity/public/images/apps/" + appId + "/" + imgIconUrl + ".jpg";
    }
}
