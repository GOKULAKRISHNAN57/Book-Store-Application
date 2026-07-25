package BridgeLabz.Book_Store_Application.auth.controller;



import BridgeLabz.Book_Store_Application.auth.dto.LoginRequest;
import BridgeLabz.Book_Store_Application.auth.dto.LoginResponse;
import BridgeLabz.Book_Store_Application.auth.dto.RegisterRequest;
import BridgeLabz.Book_Store_Application.auth.service.AuthService;
import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import BridgeLabz.Book_Store_Application.auth.dto.ForgotPasswordRequest;
import BridgeLabz.Book_Store_Application.auth.dto.ResetPasswordRequest;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register New User
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(
            @Valid
            @RequestBody
            RegisterRequest request) {

        authService.register(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User registered successfully")
                .data("Registration completed successfully")
                .build();
    }

    /**
     * Login User
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }

    /**
     * Forgot Password
     */
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset email sent successfully.")
                .data("Please check your email.")
                .build();
    }
    /**
     * Reset Password
     */
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successfully.")
                .data("Your password has been updated.")
                .build();
    }
}
