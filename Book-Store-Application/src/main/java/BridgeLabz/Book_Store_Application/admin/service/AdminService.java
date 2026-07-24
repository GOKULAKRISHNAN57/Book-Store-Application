package BridgeLabz.Book_Store_Application.admin.service;

import BridgeLabz.Book_Store_Application.admin.dto.AdminStatisticsResponse;
import BridgeLabz.Book_Store_Application.admin.dto.DashboardResponse;
import BridgeLabz.Book_Store_Application.admin.dto.OrderSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.ProductSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.UserSummaryResponse;

import java.util.List;

public interface AdminService {

    /**
     * Get dashboard summary.
     */
    DashboardResponse getDashboard();

    /**
     * Get all users.
     */
    List<UserSummaryResponse> getAllUsers();

    /**
     * Get all products.
     */
    List<ProductSummaryResponse> getAllProducts();

    /**
     * Get all orders.
     */
    List<OrderSummaryResponse> getAllOrders();

    /**
     * Get application statistics.
     */
    AdminStatisticsResponse getStatistics();

}