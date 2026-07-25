package BridgeLabz.Book_Store_Application.payment.repository;

import BridgeLabz.Book_Store_Application.enums.PaymentStatus;
import BridgeLabz.Book_Store_Application.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Find payment by payment reference.
     */
    Optional<Payment> findByPaymentReference(String paymentReference);

    /**
     * Find payment by Razorpay Order Id.
     */
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    /**
     * Find payment by Razorpay Payment Id.
     */
    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    /**
     * Find payment by Order Id.
     */
    Optional<Payment> findByOrderId(Long orderId);

    /**
     * Get all payments by status.
     */
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    long countByPaymentStatus(PaymentStatus paymentStatus);

}