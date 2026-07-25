package BridgeLabz.Book_Store_Application.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPaymentRequest {

    @NotBlank(message = "Razorpay Order Id is required")
    private String gatewayOrderId;

    @NotBlank(message = "Razorpay Payment Id is required")
    private String gatewayPaymentId;

    @NotBlank(message = "Signature is required")
    private String gatewaySignature;

}