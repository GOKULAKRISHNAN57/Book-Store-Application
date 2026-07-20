package BridgeLabz.Book_Store_Application.user.controller;



import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.user.dto.ChangePasswordRequest;
import BridgeLabz.Book_Store_Application.user.dto.UpdateProfileRequest;
import BridgeLabz.Book_Store_Application.user.dto.UserResponse;
import BridgeLabz.Book_Store_Application.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request) {

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(userService.updateProfile(id, request))
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build();
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deactivateUser(@PathVariable Long id) {

        userService.deactivateUser(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User deactivated successfully")
                .build();
    }

}
