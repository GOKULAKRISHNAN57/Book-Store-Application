package BridgeLabz.Book_Store_Application.admin.controller;

import BridgeLabz.Book_Store_Application.admin.dto.AdminStatisticsResponse;
import BridgeLabz.Book_Store_Application.admin.dto.DashboardResponse;
import BridgeLabz.Book_Store_Application.admin.dto.OrderSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.ProductSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.UserSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.service.AdminService;
import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Dashboard
     */
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {

        DashboardResponse response = adminService.getDashboard();

        return ApiResponse.success(
                "Dashboard retrieved successfully.",
                response
        );
    }

    /**
     * All Users
     */
    @GetMapping("/users")
    public ApiResponse<List<UserSummaryResponse>> getAllUsers() {

        List<UserSummaryResponse> response =
                adminService.getAllUsers();

        return ApiResponse.success(
                "Users retrieved successfully.",
                response
        );
    }

    /**
     * All Products
     */
    @GetMapping("/products")
    public ApiResponse<List<ProductSummaryResponse>> getAllProducts() {

        List<ProductSummaryResponse> response =
                adminService.getAllProducts();

        return ApiResponse.success(
                "Products retrieved successfully.",
                response
        );
    }

    /**
     * All Orders
     */
    @GetMapping("/orders")
    public ApiResponse<List<OrderSummaryResponse>> getAllOrders() {

        List<OrderSummaryResponse> response =
                adminService.getAllOrders();

        return ApiResponse.success(
                "Orders retrieved successfully.",
                response
        );
    }

    /**
     * Statistics
     */
    @GetMapping("/statistics")
    public ApiResponse<AdminStatisticsResponse> getStatistics() {

        AdminStatisticsResponse response =
                adminService.getStatistics();

        return ApiResponse.success(
                "Statistics retrieved successfully.",
                response
        );
    }

}