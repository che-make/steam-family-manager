package com.metamedicsvr.springtemplate.service.user;

import com.metamedicsvr.springtemplate.dto.auth.register.RegisterRequest;
import com.metamedicsvr.springtemplate.dto.user.UserDataResponse;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.enums.AccountStatus;
import com.metamedicsvr.springtemplate.error.exception.DuplicateEntryException;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.error.exception.UnsupportedFileException;
import com.metamedicsvr.springtemplate.repositories.user.UserRepository;
import com.metamedicsvr.springtemplate.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final S3Service s3Service;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public User getUserByUUID(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setCommonFields(User user, RegisterRequest registerRequest) {
        user.setEmail(registerRequest.getEmail().toLowerCase());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccountStatus(AccountStatus.ACTIVE);
    }

    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntryException("Duplicate entry for email: " + user.getEmail());
        }
    }

    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public UserDataResponse getUserDataResponse(UUID userId) {
        User user = getUserByUUID(userId);
        return toUserDataResponse(user);
    }

    public String uploadProfilePhoto(MultipartFile file, User user) {
        String avatarUrl;
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedFileException("Invalid file type");
        }

        if (StringUtils.hasText(user.getAvatarUrl())) {
            s3Service.deleteFileByUrl(user.getAvatarUrl());
        }

        avatarUrl = s3Service.uploadFile(file, "user/" + user.getId(), "avatar");

        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return avatarUrl;
    }



    public UserDataResponse toUserDataResponse(User user) {
        return UserDataResponse.builder()
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public String getUserPhoto(User user) {
        return user.getAvatarUrl();
    }

}
