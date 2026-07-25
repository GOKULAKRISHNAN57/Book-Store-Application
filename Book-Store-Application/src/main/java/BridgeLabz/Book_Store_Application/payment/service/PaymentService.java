package BridgeLabz.Book_Store_Application.payment.service;

import BridgeLabz.Book_Store_Application.payment.dto.*;

import java.util.List;

public interface PaymentService {

    /**
     * Create Payment
     */
    RazorpayOrderResponse createPayment(PaymentRequest request);

    /**
     * Verify Razorpay Payment
     */
    PaymentResponse verifyPayment(VerifyPaymentRequest request);

    /**
     * Get Payment By Order Id
     */
    PaymentResponse getPaymentByOrder(Long orderId);

    /**
     * Get Payment History
     */
    List<PaymentHistoryResponse> getPaymentHistory();

    void handleWebhook(
            String payload,
            String signature
    );

}