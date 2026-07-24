package BridgeLabz.Book_Store_Application.order.dto;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private Long userId;

    private Long customerProfileId;

    private Long shippingAddressId;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemResponse> orderItems;

    private LocalDateTime createdAt;

}