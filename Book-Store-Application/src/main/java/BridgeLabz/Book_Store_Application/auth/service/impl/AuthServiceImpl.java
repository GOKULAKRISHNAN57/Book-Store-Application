package BridgeLabz.Book_Store_Application.auth.service.impl;

import BridgeLabz.Book_Store_Application.auth.dto.LoginRequest;
import BridgeLabz.Book_Store_Application.auth.dto.LoginResponse;
import BridgeLabz.Book_Store_Application.auth.dto.RegisterRequest;
import BridgeLabz.Book_Store_Application.auth.mapper.AuthMapper;
import BridgeLabz.Book_Store_Application.auth.service.AuthService;
import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.DuplicateResourceException;
import BridgeLabz.Book_Store_Application.security.JwtService;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import BridgeLabz.Book_Store_Application.auth.dto.ForgotPasswordRequest;
import BridgeLabz.Book_Store_Application.auth.dto.ResetPasswordRequest;
import BridgeLabz.Book_Store_Application.auth.entity.PasswordResetToken;
import BridgeLabz.Book_Store_Application.auth.repository.PasswordResetTokenRepository;
import BridgeLabz.Book_Store_Application.auth.service.EmailService;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;

    @Override
    public void register(RegisterRequest request) {

        log.info("Register request received for {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = authMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        log.info("User registered successfully : {}", request.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        log.info("Login request received for {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException("User account is deactivated");
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        String token = jwtService.generateToken(userDetails);

        log.info("User logged in successfully : {}", user.getEmail());

        return authMapper.toLoginResponse(user, token);
    }
    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        log.info("Forgot password request received for {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        // Remove old reset token if it exists
        passwordResetTokenRepository.findByUser(user)
                .ifPresent(passwordResetTokenRepository::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                token
        );

        log.info("Password reset email sent to {}", user.getEmail());
    }
    @Override
    public void resetPassword(ResetPasswordRequest request) {

        log.info("Reset password request received.");

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(request.getToken())
                        .orElseThrow(() ->
                                new BadRequestException("Invalid reset token."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new BadRequestException(
                    "Reset token has expired."
            );
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        log.info("Password reset successfully for {}", user.getEmail());
    }
}