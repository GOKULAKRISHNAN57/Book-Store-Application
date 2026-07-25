package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatisticsResponse {

    /**
     * Order Statistics
     */
    private Long pendingOrders;

    private Long confirmedOrders;

    private Long processingOrders;

    private Long shippedOrders;

    private Long deliveredOrders;

    private Long cancelledOrders;

    /**
     * Product Statistics
     */
    private Long activeProducts;

    private Long inactiveProducts;

    /**
     * Feedback
     */
    private Double averageRating;

    /**
     * Payment Statistics
     */
    private Long pendingPayments;

    private Long successfulPayments;

    private Long failedPayments;

    private Long refundedPayments;

}