package com.metamedicsvr.springtemplate.repositories.user;

import com.metamedicsvr.springtemplate.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmailIgnoreCase(String email);
}
