package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    /**
     * User Statistics
     */
    private Long totalUsers;

    /**
     * Product Statistics
     */
    private Long totalProducts;

    /**
     * Category Statistics
     */
    private Long totalCategories;

    /**
     * Order Statistics
     */
    private Long totalOrders;

    /**
     * Feedback Statistics
     */
    private Long totalFeedbacks;

    /**
     * Payment Statistics
     */
    private Long totalPayments;

    private Long successfulPayments;

    private Long failedPayments;

    /**
     * Revenue
     */
    private BigDecimal totalRevenue;

}