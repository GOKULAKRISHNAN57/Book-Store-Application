package BridgeLabz.Book_Store_Application.customer.repository;

import BridgeLabz.Book_Store_Application.customer.entity.CustomerProfile;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    /**
     * Find customer profile by user.
     */
    Optional<CustomerProfile> findByUser(User user);

    /**
     * Find customer profile by user id.
     */
    Optional<CustomerProfile> findByUserId(Long userId);

    /**
     * Check whether profile exists for a user.
     */
    boolean existsByUser(User user);

    /**
     * Check whether profile exists using user id.
     */
    boolean existsByUserId(Long userId);

}