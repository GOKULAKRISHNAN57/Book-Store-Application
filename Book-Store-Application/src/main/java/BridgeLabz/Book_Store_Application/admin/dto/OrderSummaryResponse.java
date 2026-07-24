package BridgeLabz.Book_Store_Application.admin.dto;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryResponse {

    private Long orderId;

    private Long userId;

    private String customerName;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

}