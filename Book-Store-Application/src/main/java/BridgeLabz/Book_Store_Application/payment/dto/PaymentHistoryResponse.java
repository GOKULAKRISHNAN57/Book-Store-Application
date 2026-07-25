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
public class PaymentHistoryResponse {

    private Long paymentId;

    private String paymentReference;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

}