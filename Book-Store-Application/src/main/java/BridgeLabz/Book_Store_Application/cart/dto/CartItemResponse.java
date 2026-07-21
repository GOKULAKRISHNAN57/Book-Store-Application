package BridgeLabz.Book_Store_Application.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long productId;

    private String productTitle;

    private String productAuthor;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}