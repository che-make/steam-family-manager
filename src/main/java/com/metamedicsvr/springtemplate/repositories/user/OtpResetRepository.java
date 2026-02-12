package com.metamedicsvr.springtemplate.repositories.user;

import com.metamedicsvr.springtemplate.entities.user.OtpReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpResetRepository extends JpaRepository<OtpReset, UUID> {
    Optional<OtpReset> findByResetToken(String resetToken);
}
