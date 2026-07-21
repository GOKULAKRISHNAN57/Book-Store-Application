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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtService jwtService;

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
}