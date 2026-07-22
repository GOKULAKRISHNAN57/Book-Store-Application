package BridgeLabz.Book_Store_Application.feedback.repository;

import BridgeLabz.Book_Store_Application.feedback.entity.Feedback;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByProductId(Long productId);

    Optional<Feedback> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    long countByProductId(Long productId);
}