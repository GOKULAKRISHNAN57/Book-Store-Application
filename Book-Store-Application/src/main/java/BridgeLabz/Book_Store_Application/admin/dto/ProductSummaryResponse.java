package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSummaryResponse {

    private Long productId;

    private String title;

    private String author;

    private BigDecimal price;

    private Integer stockQuantity;

    private Boolean active;

}