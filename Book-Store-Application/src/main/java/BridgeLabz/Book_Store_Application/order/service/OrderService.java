package BridgeLabz.Book_Store_Application.order.service;

import BridgeLabz.Book_Store_Application.order.dto.OrderResponse;
import BridgeLabz.Book_Store_Application.order.dto.PlaceOrderRequest;

import java.util.List;

public interface OrderService {

    /**
     * Place a new order.
     *
     * @param userId Logged-in user ID
     * @param request Shipping address information
     * @return Created order
     */
    OrderResponse placeOrder(
            Long userId,
            PlaceOrderRequest request
    );

    /**
     * Get all orders of the logged-in user.
     *
     * @param userId Logged-in user ID
     * @return List of orders
     */
    List<OrderResponse> getMyOrders(Long userId);

    /**
     * Get a particular order.
     *
     * @param userId Logged-in user ID
     * @param orderId Order ID
     * @return Order details
     */
    OrderResponse getOrderById(
            Long userId,
            Long orderId
    );

    /**
     * Cancel an order.
     *
     * @param userId Logged-in user ID
     * @param orderId Order ID
     * @return Updated order
     */
    OrderResponse cancelOrder(
            Long userId,
            Long orderId
    );

}