package BridgeLabz.Book_Store_Application.user.service.impl;



import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.user.dto.ChangePasswordRequest;
import BridgeLabz.Book_Store_Application.user.dto.UpdateProfileRequest;
import BridgeLabz.Book_Store_Application.user.dto.UserResponse;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.mapper.UserMapper;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import BridgeLabz.Book_Store_Application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        log.info("Fetching user with id {}", id);

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        return mapper.toResponse(user);
    }

    @Override
    public UserResponse updateProfile(Long id,
                                      UpdateProfileRequest request) {

        log.info("Updating profile for user {}", id);

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        mapper.updateProfile(request, user);

        repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public void changePassword(Long id,
                               ChangePasswordRequest request) {

        log.info("Changing password for user {}", id);

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }

    @Override
    public void deactivateUser(Long id) {

        log.info("Deactivating user {}", id);

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        user.setActive(false);

        repository.save(user);
    }

}
