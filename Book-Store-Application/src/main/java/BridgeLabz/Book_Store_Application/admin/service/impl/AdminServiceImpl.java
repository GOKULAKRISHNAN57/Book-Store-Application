package BridgeLabz.Book_Store_Application.admin.service.impl;

import BridgeLabz.Book_Store_Application.admin.dto.AdminStatisticsResponse;
import BridgeLabz.Book_Store_Application.admin.dto.DashboardResponse;
import BridgeLabz.Book_Store_Application.admin.dto.OrderSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.ProductSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.dto.UserSummaryResponse;
import BridgeLabz.Book_Store_Application.admin.mapper.AdminMapper;
import BridgeLabz.Book_Store_Application.admin.service.AdminService;
import BridgeLabz.Book_Store_Application.category.repository.CategoryRepository;
import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import BridgeLabz.Book_Store_Application.feedback.repository.FeedbackRepository;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.order.repository.OrderRepository;
import BridgeLabz.Book_Store_Application.payment.repository.PaymentRepository;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import BridgeLabz.Book_Store_Application.enums.PaymentStatus;
import BridgeLabz.Book_Store_Application.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final OrderRepository orderRepository;

    private final FeedbackRepository feedbackRepository;

    private final AdminMapper adminMapper;
    private final PaymentRepository paymentRepository;


    @Override
    public DashboardResponse getDashboard() {

        long totalUsers = userRepository.count();

        long totalProducts = productRepository.count();

        long totalCategories = categoryRepository.count();

        long totalOrders = orderRepository.count();

        long totalFeedbacks = feedbackRepository.count();

        long totalPayments = paymentRepository.count();

        long successfulPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.SUCCESS);

        long failedPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.FAILED);

        BigDecimal totalRevenue = orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalCategories(totalCategories)
                .totalOrders(totalOrders)
                .totalFeedbacks(totalFeedbacks)
                .totalPayments(totalPayments)
                .successfulPayments(successfulPayments)
                .failedPayments(failedPayments)
                .totalRevenue(totalRevenue)
                .build();
    }

    @Override
    public List<UserSummaryResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return adminMapper.toUserSummaryList(users);
    }

    @Override
    public List<ProductSummaryResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return adminMapper.toProductSummaryList(products);
    }
    @Override
    public List<OrderSummaryResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return adminMapper.toOrderSummaryList(orders);
    }
    @Override
    public AdminStatisticsResponse getStatistics() {

        long pendingOrders =
                orderRepository.countByOrderStatus(OrderStatus.PENDING);

        long confirmedOrders =
                orderRepository.countByOrderStatus(OrderStatus.CONFIRMED);

        long processingOrders =
                orderRepository.countByOrderStatus(OrderStatus.PROCESSING);

        long shippedOrders =
                orderRepository.countByOrderStatus(OrderStatus.SHIPPED);

        long deliveredOrders =
                orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

        long cancelledOrders =
                orderRepository.countByOrderStatus(OrderStatus.CANCELLED);

        long activeProducts = productRepository.findAll()
                .stream()
                .filter(Product::getActive)
                .count();

        long inactiveProducts = productRepository.findAll()
                .stream()
                .filter(product -> !product.getActive())
                .count();

        Double averageRating = feedbackRepository.findAll()
                .stream()
                .mapToInt(feedback -> feedback.getRating())
                .average()
                .orElse(0.0);

        long pendingPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.PENDING);

        long successfulPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.SUCCESS);

        long failedPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.FAILED);

        long refundedPayments =
                paymentRepository.countByPaymentStatus(PaymentStatus.REFUNDED);

        return AdminStatisticsResponse.builder()
                .pendingOrders(pendingOrders)
                .confirmedOrders(confirmedOrders)
                .processingOrders(processingOrders)
                .shippedOrders(shippedOrders)
                .deliveredOrders(deliveredOrders)
                .cancelledOrders(cancelledOrders)
                .activeProducts(activeProducts)
                .inactiveProducts(inactiveProducts)
                .averageRating(averageRating)
                .pendingPayments(pendingPayments)
                .successfulPayments(successfulPayments)
                .failedPayments(failedPayments)
                .refundedPayments(refundedPayments)
                .build();
    }
}