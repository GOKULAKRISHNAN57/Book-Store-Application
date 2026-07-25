package BridgeLabz.Book_Store_Application.auth.repository;

import BridgeLabz.Book_Store_Application.auth.entity.PasswordResetToken;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Find token.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Find token by user.
     */
    Optional<PasswordResetToken> findByUser(User user);

    /**
     * Delete token after successful password reset.
     */
    void deleteByUser(User user);

    /**
     * Delete token using token string.
     */
    void deleteByToken(String token);

    boolean existsByToken(String token);

}