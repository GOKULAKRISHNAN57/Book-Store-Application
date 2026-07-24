package BridgeLabz.Book_Store_Application.order.repository;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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

}