package BridgeLabz.Book_Store_Application.cart.repository;

import BridgeLabz.Book_Store_Application.cart.entity.Cart;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Find cart by user.
     */
    Optional<Cart> findByUser(User user);

    /**
     * Find cart by user id.
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Check whether cart exists for a user.
     */
    boolean existsByUserId(Long userId);

}