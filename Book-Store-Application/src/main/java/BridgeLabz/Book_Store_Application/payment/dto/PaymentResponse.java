package BridgeLabz.Book_Store_Application.payment.dto;

import BridgeLabz.Book_Store_Application.enums.PaymentMethod;
import BridgeLabz.Book_Store_Application.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private String paymentReference;

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private BigDecimal amount;

    private String currency;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private Long orderId;

    private LocalDateTime createdAt;

}