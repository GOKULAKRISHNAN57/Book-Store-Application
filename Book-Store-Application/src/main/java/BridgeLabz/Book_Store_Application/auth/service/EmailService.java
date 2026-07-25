package BridgeLabz.Book_Store_Application.auth.service;

public interface EmailService {

    /**
     * Send password reset email.
     *
     * @param email Recipient email
     * @param token Password reset token
     */
    void sendPasswordResetEmail(
            String email,
            String token
    );

}