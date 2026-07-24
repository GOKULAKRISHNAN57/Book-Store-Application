package BridgeLabz.Book_Store_Application.order.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.order.dto.OrderResponse;
import BridgeLabz.Book_Store_Application.order.dto.PlaceOrderRequest;
import BridgeLabz.Book_Store_Application.order.service.OrderService;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Place a new order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> placeOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody PlaceOrderRequest request) {

        OrderResponse response = orderService.placeOrder(
                userDetails.getId(),
                request
        );

        return ApiResponse.success(
                "Order placed successfully.",
                response
        );
    }

    /**
     * Get all orders of logged-in user
     */
    @GetMapping
    public ApiResponse<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<OrderResponse> response = orderService.getMyOrders(
                userDetails.getId()
        );

        return ApiResponse.success(
                "Orders fetched successfully.",
                response
        );
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {

        OrderResponse response = orderService.getOrderById(
                userDetails.getId(),
                orderId
        );

        return ApiResponse.success(
                "Order fetched successfully.",
                response
        );
    }

    /**
     * Cancel order
     */
    @PutMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {

        OrderResponse response = orderService.cancelOrder(
                userDetails.getId(),
                orderId
        );

        return ApiResponse.success(
                "Order cancelled successfully.",
                response
        );
    }

}