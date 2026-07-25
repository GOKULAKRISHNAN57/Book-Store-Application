package BridgeLabz.Book_Store_Application.payment.mapper;

import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentHistoryResponse;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentRequest;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentResponse;
import BridgeLabz.Book_Store_Application.payment.entity.Payment;
import org.springframework.stereotype.Component;
import BridgeLabz.Book_Store_Application.enums.PaymentStatus;

import java.util.UUID;

@Component
public class PaymentMapper {

    /**
     * Convert PaymentRequest -> Payment Entity
     */
    public Payment toEntity(PaymentRequest request, Order order) {

        return Payment.builder()
                .paymentReference(generatePaymentReference())
                .amount(order.getTotalAmount())
                .currency("INR")
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .order(order)
                .build();
    }

    /**
     * Convert Payment Entity -> PaymentResponse
     */
    public PaymentResponse toResponse(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentReference(payment.getPaymentReference())
                .gatewayOrderId(payment.getGatewayOrderId())
                .gatewayPaymentId(payment.getGatewayPaymentId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .orderId(payment.getOrder().getId())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    /**
     * Convert Payment Entity -> PaymentHistoryResponse
     */
    public PaymentHistoryResponse toHistoryResponse(Payment payment) {

        return PaymentHistoryResponse.builder()
                .paymentId(payment.getId())
                .paymentReference(payment.getPaymentReference())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getCreatedAt())
                .build();
    }

    /**
     * Generate Payment Reference
     */
    private String generatePaymentReference() {

        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

}