package com.metamedicsvr.springtemplate.service.passwordreset;

import com.metamedicsvr.springtemplate.enums.Language;
import com.metamedicsvr.springtemplate.service.email.EmailService;
import com.metamedicsvr.springtemplate.error.exception.ExpirePasswordTokenException;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.entities.user.OtpReset;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.repositories.user.OtpResetRepository;
import com.metamedicsvr.springtemplate.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final OtpResetRepository otpResetRepository;

    private final EmailService emailService;

    private final UserService userService;

    @Value("${password.reset.token.expiry}")
    private Integer resetTokenExpiryHours;

    @Value("${otp.expiry}")
    private Integer otpExpiryHours;

    private final Random random = new Random();


    public void sendOtp(String email, Language language) throws MessagingException {
        User user = userService.getUserByEmail(email);

        String otp = generateOTP(user);

        emailService.sendOtpEmail(user, otp, language, otpExpiryHours.toString());
    }

    private String generateOTP(User user) {
        OtpReset otpReset = OtpReset.builder()
                .id(user.getId())
                .user(user)
                .otp(String.valueOf(random.nextInt(100000, 1000000)))
                .otpExpiryDate(LocalDateTime.now().plusHours(otpExpiryHours))
                .build();

        otpResetRepository.save(otpReset);

        return otpReset.getOtp();
    }

    public void resetPasswordRequest(String token, String newPassword) {
        // Find user by token
        OtpReset otpReset = otpResetRepository.findByResetToken(token)
                .orElseThrow(() -> new NotFoundException("Token not found"));

        User user = userService.getUserByEmail(otpReset.getUser().getEmail());

        // Check if token is expired
        if (otpReset.getTokenExpiryDate().isAfter(LocalDateTime.now())) {
            changePassword(user, otpReset, newPassword);
        } else {
            throw new ExpirePasswordTokenException("The token is expired, please request a new one");
        }
    }

    private String generatePasswordResetToken(User user) {
        // Generate a random token and save it to the user
        String token = UUID.randomUUID().toString();
        OtpReset otpReset = otpResetRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("OtpReset not found"));

        //TODO: Change to builder
        otpReset.setResetToken(token);
        otpReset.setTokenExpiryDate(LocalDateTime.now().plusHours(resetTokenExpiryHours));
        otpResetRepository.save(otpReset);

        return token;
    }

    private void changePassword(User user, OtpReset otpReset, String newPassword) {
        // Change the password of the user and remove the token
        userService.changeUserPassword(user, newPassword);

        otpReset.setResetToken(null);
        otpReset.setTokenExpiryDate(null);
        otpResetRepository.delete(otpReset);
    }

    public String receiveOtp(String email, String otpCode) {
        User user = userService.getUserByEmail(email);

        OtpReset otpReset = otpResetRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("OtpReset not found"));

        if (otpReset.getOtp().equals(otpCode) && otpReset.getOtpExpiryDate().isAfter(LocalDateTime.now())) {
            return generatePasswordResetToken(user);
        } else {
            throw new ExpirePasswordTokenException("The OTP is invalid or expired");
        }
    }

}

