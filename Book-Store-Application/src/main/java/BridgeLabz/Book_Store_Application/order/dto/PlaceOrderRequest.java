package BridgeLabz.Book_Store_Application.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {

    @NotNull(message = "Shipping address ID is required.")
    private Long shippingAddressId;

}