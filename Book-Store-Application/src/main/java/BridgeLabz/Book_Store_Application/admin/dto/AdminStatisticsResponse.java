package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatisticsResponse {

    private Long totalUsers;

    private Long totalProducts;

    private Long totalOrders;

    private Long pendingOrders;

    private Long deliveredOrders;

    private Long cancelledOrders;

    private BigDecimal totalRevenue;

    private Double averageProductRating;

}