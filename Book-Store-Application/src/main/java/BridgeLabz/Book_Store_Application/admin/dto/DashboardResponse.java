package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private Long totalUsers;

    private Long totalProducts;

    private Long totalOrders;

    private Long totalCategories;

    private BigDecimal totalRevenue;

}