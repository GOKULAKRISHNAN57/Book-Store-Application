package BridgeLabz.Book_Store_Application.order.repository;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import BridgeLabz.Book_Store_Application.enums.PaymentStatus;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import BridgeLabz.Book_Store_Application.enums.OrderStatus;


import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Get all orders of a user.
     */
    List<Order> findByUser(User user);

    /**
     * Get all orders using user ID.
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find a particular order of a user.
     */
    Optional<Order> findByIdAndUser(Long orderId, User user);

    /**
     * Find a particular order using user ID.
     */
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    /**
     * Get orders by status.
     */
    List<Order> findByOrderStatus(OrderStatus orderStatus);

    /**
     * Get user's orders by status.
     */
    List<Order> findByUserIdAndOrderStatus(
            Long userId,
            OrderStatus orderStatus
    );


    @Query("""
        SELECT COUNT(o) > 0
        FROM Order o
        JOIN o.orderItems oi
        WHERE o.user.id = :userId
          AND oi.product.id = :productId
          AND o.orderStatus = BridgeLabz.Book_Store_Application.enums.OrderStatus.DELIVERED
        """)
    boolean hasPurchasedProduct(
            @Param("userId") Long userId,
            @Param("productId") Long productId
    );
    long countByPayment_PaymentStatus(PaymentStatus paymentStatus);
    long countByOrderStatus(OrderStatus orderStatus);
}