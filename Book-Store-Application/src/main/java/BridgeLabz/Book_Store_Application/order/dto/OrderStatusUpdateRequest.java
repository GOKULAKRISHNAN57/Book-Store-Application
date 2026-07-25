package BridgeLabz.Book_Store_Application.order.dto;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequest {

    @NotNull(message = "Order status is required.")
    private OrderStatus orderStatus;

}