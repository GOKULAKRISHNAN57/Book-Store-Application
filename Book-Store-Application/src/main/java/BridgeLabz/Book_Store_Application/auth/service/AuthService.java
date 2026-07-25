package BridgeLabz.Book_Store_Application.auth.service;


import BridgeLabz.Book_Store_Application.auth.dto.*;

public interface AuthService {

    /**
     * Register a new user
     */
    void register(RegisterRequest request);

    /**
     * Login user and return JWT token
     */
    LoginResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
