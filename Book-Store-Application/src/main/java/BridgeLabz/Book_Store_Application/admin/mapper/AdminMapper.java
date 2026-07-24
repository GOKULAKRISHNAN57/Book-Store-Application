package BridgeLabz.Book_Store_Application.admin.mapper;

import BridgeLabz.Book_Store_Application.admin.dto.OrderSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.ProductSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.UserSummaryResponse;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AdminMapper {

    /**
     * Convert User Entity to UserSummaryResponse
     */
    public UserSummaryResponse toUserSummary(User user) {

        if (user == null) {
            return null;
        }

        return UserSummaryResponse.builder()
                .userId(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.getActive())
                .build();
    }

    /**
     * Convert Product Entity to ProductSummaryResponse
     */
    public ProductSummaryResponse toProductSummary(Product product) {

        if (product == null) {
            return null;
        }

        return ProductSummaryResponse.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .author(product.getAuthor())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .active(product.getActive())
                .build();
    }

    /**
     * Convert Order Entity to OrderSummaryResponse
     */
    public OrderSummaryResponse toOrderSummary(Order order) {

        if (order == null) {
            return null;
        }

        return OrderSummaryResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .customerName(order.getUser().getFullName())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    /**
     * Convert User List
     */
    public List<UserSummaryResponse> toUserSummaryList(
            List<User> users) {

        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::toUserSummary)
                .toList();
    }

    /**
     * Convert Product List
     */
    public List<ProductSummaryResponse> toProductSummaryList(
            List<Product> products) {

        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(this::toProductSummary)
                .toList();
    }

    /**
     * Convert Order List
     */
    public List<OrderSummaryResponse> toOrderSummaryList(
            List<Order> orders) {

        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(this::toOrderSummary)
                .toList();
    }

}