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
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    public DashboardResponse getDashboard() {

        long totalUsers = userRepository.count();

        long totalProducts = productRepository.count();

        long totalCategories = categoryRepository.count();

        long totalOrders = orderRepository.count();

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

        long totalUsers = userRepository.count();

        long totalProducts = productRepository.count();

        long totalOrders = orderRepository.count();

        long pendingOrders = orderRepository
                .findByOrderStatus(OrderStatus.PENDING)
                .size();

        long deliveredOrders = orderRepository
                .findByOrderStatus(OrderStatus.DELIVERED)
                .size();

        long cancelledOrders = orderRepository
                .findByOrderStatus(OrderStatus.CANCELLED)
                .size();

        BigDecimal totalRevenue = orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double averageRating = feedbackRepository
                .findAll()
                .stream()
                .mapToInt(feedback -> feedback.getRating())
                .average()
                .orElse(0.0);

        return AdminStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .deliveredOrders(deliveredOrders)
                .cancelledOrders(cancelledOrders)
                .totalRevenue(totalRevenue)
                .averageProductRating(
                        Math.round(averageRating * 10.0) / 10.0
                )
                .build();
    }
}