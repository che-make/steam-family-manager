package com.metamedicsvr.springtemplate.service.email;

import com.metamedicsvr.springtemplate.enums.Language;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.error.exception.LanguageNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public void sendOtpEmail(User user, String otp, Language language, String urlExpiryHours) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String subject;
        String templateName;

        helper.setTo(user.getEmail());

        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("OTP", otp);
        context.setVariable("urlExpiryHours", urlExpiryHours);


        switch (language) {
            case ENGLISH:
                subject = "Reset your APP NAME password";
                templateName = "otp-email-en";
                break;
            case SPANISH:
                subject = "Restablecer tu contraseña de APP NAME";
                templateName = "otp-email-es";
                break;
            case SOMALI:
                subject = "Dib u celi furahaaga APP NAME";
                templateName = "otp-email-so";
                break;
            default:
                throw new LanguageNotFoundException("Unsupported language: " + language);
        }

        String htmlBody = templateEngine.process("email/otp/" + templateName, context);
        helper.setText(htmlBody, true);
        helper.setSubject(subject);

        mailSender.send(message);
    }

}

