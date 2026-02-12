package com.metamedicsvr.springtemplate.entities.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpReset {

    @Id
    private UUID id;

    private String resetToken;

    private String otp;

    private LocalDateTime tokenExpiryDate;

    private LocalDateTime otpExpiryDate;

    @OneToOne
    @MapsId
    private User user;

}
