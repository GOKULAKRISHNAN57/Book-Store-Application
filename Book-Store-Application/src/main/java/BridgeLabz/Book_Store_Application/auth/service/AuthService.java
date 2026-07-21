package BridgeLabz.Book_Store_Application.auth.service;


import BridgeLabz.Book_Store_Application .auth.dto.LoginRequest;
import BridgeLabz.Book_Store_Application .auth.dto.LoginResponse;
import BridgeLabz.Book_Store_Application.auth.dto.RegisterRequest;

public interface AuthService {

    /**
     * Register a new user
     */
    void register(RegisterRequest request);

    /**
     * Login user and return JWT token
     */
    LoginResponse login(LoginRequest request);

}
