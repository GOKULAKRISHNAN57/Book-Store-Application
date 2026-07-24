package BridgeLabz.Book_Store_Application.feedback.repository;

import BridgeLabz.Book_Store_Application.feedback.entity.Feedback;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Get all feedback for a product.
     */
    List<Feedback> findByProduct(Product product);

    /**
     * Get all feedback by product ID.
     */
    List<Feedback> findByProductId(Long productId);

    /**
     * Find feedback submitted by a user for a product.
     */
    Optional<Feedback> findByUserAndProduct(
            User user,
            Product product
    );

    /**
     * Find feedback by ID and user.
     * Used for update and delete.
     */
    Optional<Feedback> findByIdAndUser(
            Long id,
            User user
    );

    /**
     * Check whether the user has already reviewed the product.
     */
    boolean existsByUserAndProduct(
            User user,
            Product product
    );

    /**
     * Count total reviews for a product.
     */
    long countByProduct(Product product);

    @Query("""
       SELECT AVG(f.rating)
       FROM Feedback f
       WHERE f.product.id = :productId
       """)
    Double getAverageRating(@Param("productId") Long productId);

}