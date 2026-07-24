package BridgeLabz.Book_Store_Application.order.mapper;

import BridgeLabz.Book_Store_Application.order.dto.OrderItemResponse;
import BridgeLabz.Book_Store_Application.order.dto.OrderResponse;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    /**
     * Convert Order entity to OrderResponse DTO.
     */
    public OrderResponse toResponse(Order order) {

        if (order == null) {
            return null;
        }

        List<OrderItemResponse> orderItems =
                order.getOrderItems() == null
                        ? Collections.emptyList()
                        : order.getOrderItems()
                        .stream()
                        .map(this::toOrderItemResponse)
                        .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .customerProfileId(order.getCustomerProfile().getId())
                .shippingAddressId(order.getShippingAddress().getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItems)
                .createdAt(order.getCreatedAt())
                .build();
    }

    /**
     * Convert OrderItem entity to OrderItemResponse DTO.
     */
    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        if (orderItem == null) {
            return null;
        }

        return OrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getTitle())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

}