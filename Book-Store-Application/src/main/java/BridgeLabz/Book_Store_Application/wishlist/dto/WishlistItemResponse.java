package BridgeLabz.Book_Store_Application.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {

    private Long productId;

    private String title;

    private String author;

    private String imageUrl;

    private BigDecimal price;

}