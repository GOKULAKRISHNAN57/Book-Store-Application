package BridgeLabz.Book_Store_Application.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayOrderResponse {

    /**
     * Razorpay Order Id
     */
    private String orderId;

    /**
     * Amount in Paise
     * Example:
     * ₹500 = 50000
     */
    private Long amount;

    /**
     * Currency
     */
    private String currency;

    /**
     * Internal Payment Reference
     */
    private String paymentReference;

    /**
     * Razorpay Key Id
     */
    private String key;

}