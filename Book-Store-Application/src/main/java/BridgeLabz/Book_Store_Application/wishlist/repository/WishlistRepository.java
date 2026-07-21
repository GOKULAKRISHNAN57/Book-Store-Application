package BridgeLabz.Book_Store_Application.wishlist.repository;

import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * Find wishlist by user.
     */
    Optional<Wishlist> findByUser(User user);

    /**
     * Find wishlist by user id.
     */
    Optional<Wishlist> findByUserId(Long userId);

    /**
     * Check whether wishlist exists for a user.
     */
    boolean existsByUserId(Long userId);

}