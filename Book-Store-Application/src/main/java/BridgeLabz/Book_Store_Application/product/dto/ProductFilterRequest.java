package BridgeLabz.Book_Store_Application.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilterRequest {

    /**
     * Search keyword
     */
    private String keyword;

    /**
     * Category
     */
    private Long categoryId;

    /**
     * Minimum price
     */
    private BigDecimal minPrice;

    /**
     * Maximum price
     */
    private BigDecimal maxPrice;

    /**
     * Active products only
     */
    private Boolean active;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "title";
    private String direction = "asc";

}