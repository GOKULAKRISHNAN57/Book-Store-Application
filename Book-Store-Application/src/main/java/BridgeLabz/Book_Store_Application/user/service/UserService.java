package BridgeLabz.Book_Store_Application.user.service;


import BridgeLabz.Book_Store_Application.user.dto.ChangePasswordRequest;
import BridgeLabz.Book_Store_Application.user.dto.UpdateProfileRequest;
import BridgeLabz.Book_Store_Application.user.dto.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse updateProfile(Long id, UpdateProfileRequest request);

    void changePassword(Long id, ChangePasswordRequest request);

    void deactivateUser(Long id);

}
