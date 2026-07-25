package BridgeLabz.Book_Store_Application.auth.entity;

import BridgeLabz.Book_Store_Application.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reset Token (UUID)
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * User
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Token Expiry Time
     */
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Created Time
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}