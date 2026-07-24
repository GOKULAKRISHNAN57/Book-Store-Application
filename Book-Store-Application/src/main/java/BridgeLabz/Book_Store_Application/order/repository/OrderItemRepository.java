package BridgeLabz.Book_Store_Application.order.repository;

import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Get all items of an order.
     */
    List<OrderItem> findByOrder(Order order);

    /**
     * Get all items using order ID.
     */
    List<OrderItem> findByOrderId(Long orderId);

}