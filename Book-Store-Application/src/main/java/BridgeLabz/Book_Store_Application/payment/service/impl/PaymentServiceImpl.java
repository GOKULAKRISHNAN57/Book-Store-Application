package BridgeLabz.Book_Store_Application.payment.service.impl;

import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import BridgeLabz.Book_Store_Application.enums.PaymentMethod;
import BridgeLabz.Book_Store_Application.enums.PaymentStatus;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.order.repository.OrderRepository;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentHistoryResponse;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentRequest;
import BridgeLabz.Book_Store_Application.payment.dto.PaymentResponse;
import BridgeLabz.Book_Store_Application.payment.dto.RazorpayOrderResponse;
import BridgeLabz.Book_Store_Application.payment.dto.VerifyPaymentRequest;
import BridgeLabz.Book_Store_Application.payment.entity.Payment;
import BridgeLabz.Book_Store_Application.payment.mapper.PaymentMapper;
import BridgeLabz.Book_Store_Application.payment.repository.PaymentRepository;
import BridgeLabz.Book_Store_Application.payment.service.PaymentService;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @Override
    public RazorpayOrderResponse createPayment(
            PaymentRequest request) {

        log.info("Creating payment for Order : {}",
                request.getOrderId());

        Order order = orderRepository.findById(
                request.getOrderId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Order not found with id : "
                                + request.getOrderId()));

        Payment payment =
                paymentMapper.toEntity(request, order);

        /*
         * Cash On Delivery
         */
        if (request.getPaymentMethod()
                == PaymentMethod.CASH_ON_DELIVERY) {

            payment.setPaymentStatus(
                    PaymentStatus.PENDING
            );

            paymentRepository.save(payment);

            order.setOrderStatus(
                    OrderStatus.CONFIRMED
            );

            orderRepository.save(order);

            return RazorpayOrderResponse.builder()
                    .paymentReference(
                            payment.getPaymentReference()
                    )
                    .amount(
                            payment.getAmount()
                                    .multiply(
                                            java.math.BigDecimal.valueOf(100)
                                    ).longValue()
                    )
                    .currency("INR")
                    .build();
        }

        /*
         * Razorpay Payment
         */
        try {

            JSONObject options = new JSONObject();

            options.put(
                    "amount",
                    payment.getAmount()
                            .multiply(
                                    java.math.BigDecimal.valueOf(100)
                            ).longValue()
            );

            options.put(
                    "currency",
                    "INR"
            );

            options.put(
                    "receipt",
                    payment.getPaymentReference()
            );

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(options);

            payment.setGatewayOrderId(
                    razorpayOrder.get("id")
            );

            paymentRepository.save(payment);

            return RazorpayOrderResponse.builder()
                    .orderId(
                            razorpayOrder.get("id")
                    )
                    .amount(
                            razorpayOrder.get("amount")
                    )
                    .currency(
                            razorpayOrder.get("currency")
                    )
                    .paymentReference(
                            payment.getPaymentReference()
                    )
                    .key(
                            razorpayKey
                    )
                    .build();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create Razorpay Order",
                    e
            );
        }
    }

    @Override
    public PaymentResponse verifyPayment(
            VerifyPaymentRequest request) {

        log.info("Verifying Razorpay Payment");

        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getGatewayOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found."));

        boolean verified = verifySignature(
                request.getGatewayOrderId(),
                request.getGatewayPaymentId(),
                request.getGatewaySignature()
        );

        if (!verified) {
            throw new RuntimeException(
                    "Invalid Razorpay Signature.");
        }

        payment.setGatewayPaymentId(
                request.getGatewayPaymentId());

        payment.setGatewaySignature(
                request.getGatewaySignature());

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS);

        Order order = payment.getOrder();

        order.setOrderStatus(
                OrderStatus.CONFIRMED);

        orderRepository.save(order);

        paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }
    private boolean verifySignature(
            String orderId,
            String paymentId,
            String signature) {

        try {

            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            razorpaySecret.getBytes(),
                            "HmacSHA256"
                    );

            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes());

            StringBuilder generatedSignature = new StringBuilder();

            for (byte b : hash) {
                generatedSignature.append(
                        String.format("%02x", b)
                );
            }

            return generatedSignature
                    .toString()
                    .equals(signature);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Signature verification failed.",
                    e
            );
        }
    }
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrder(Long orderId) {

        log.info("Fetching payment for order : {}", orderId);

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id : " + orderId
                        ));

        return paymentMapper.toResponse(payment);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryResponse> getPaymentHistory() {

        log.info("Fetching payment history");

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toHistoryResponse)
                .toList();
    }

    @Override
    public void handleWebhook(
            String payload,
            String signature) {

    }

}