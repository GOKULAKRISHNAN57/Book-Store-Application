package BridgeLabz.Book_Store_Application.auth.service.impl;

import BridgeLabz.Book_Store_Application.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(
            String email,
            String token) {

        String resetLink =
                "http://localhost:8080/api/auth/reset-password?token="
                        + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(email);

        message.setSubject("Password Reset Request");

        message.setText(
                """
                Hello,

                We received a request to reset your password.

                Click the link below to reset your password:

                %s

                This link will expire in 30 minutes.

                If you did not request this, please ignore this email.

                Regards,
                Book Store Team
                """.formatted(resetLink)
        );

        mailSender.send(message);
    }
}