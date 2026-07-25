package BridgeLabz.Book_Store_Application.payment.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentHistoryResponse;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentRequest;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentResponse;
import BridgeLabz.Book_Store_Application.payment.dto.RazorpayOrderResponse;
import BridgeLabz.Book_Store_Application.payment.dto.VerifyPaymentRequest;
import BridgeLabz.Book_Store_Application.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Create Payment
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RazorpayOrderResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        RazorpayOrderResponse response =
                paymentService.createPayment(request);

        return ApiResponse.success(
                "Payment created successfully.",
                response
        );
    }

    /**
     * Verify Razorpay Payment
     */
    @PostMapping("/verify")
    public ApiResponse<PaymentResponse> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request) {

        PaymentResponse response =
                paymentService.verifyPayment(request);

        return ApiResponse.success(
                "Payment verified successfully.",
                response
        );
    }

    /**
     * Get Payment By Order Id
     */
    @GetMapping("/order/{orderId}")
    public ApiResponse<PaymentResponse> getPaymentByOrder(
            @PathVariable Long orderId) {

        PaymentResponse response =
                paymentService.getPaymentByOrder(orderId);

        return ApiResponse.success(
                "Payment fetched successfully.",
                response
        );
    }

    /**
     * Payment History
     */
    @GetMapping("/history")
    public ApiResponse<List<PaymentHistoryResponse>> getPaymentHistory() {

        List<PaymentHistoryResponse> response =
                paymentService.getPaymentHistory();

        return ApiResponse.success(
                "Payment history fetched successfully.",
                response
        );
    }

}