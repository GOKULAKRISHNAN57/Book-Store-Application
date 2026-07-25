package BridgeLabz.Book_Store_Application.payment.webhook;

import BridgeLabz.Book_Store_Application.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/razorpay")
    public ResponseEntity<String> razorpayWebhook(

            @RequestHeader("X-Razorpay-Signature")
            String signature,

            @RequestBody
            String payload) {

        paymentService.handleWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok("Webhook Received");
    }

}