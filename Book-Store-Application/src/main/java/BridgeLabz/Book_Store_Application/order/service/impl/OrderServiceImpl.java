package BridgeLabz.Book_Store_Application.order.service.impl;

import BridgeLabz.Book_Store_Application.cart.entity.Cart;
import BridgeLabz.Book_Store_Application.cart.entity.CartItem;
import BridgeLabz.Book_Store_Application.cart.repository.CartItemRepository;
import BridgeLabz.Book_Store_Application.cart.repository.CartRepository;
import BridgeLabz.Book_Store_Application.customer.entity.Address;
import BridgeLabz.Book_Store_Application.customer.entity.CustomerProfile;
import BridgeLabz.Book_Store_Application.customer.repository.AddressRepository;
import BridgeLabz.Book_Store_Application.customer.repository.CustomerProfileRepository;
import BridgeLabz.Book_Store_Application.enums.OrderStatus;
import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.order.dto.OrderResponse;
import BridgeLabz.Book_Store_Application.order.dto.PlaceOrderRequest;
import BridgeLabz.Book_Store_Application.order.entity.Order;
import BridgeLabz.Book_Store_Application.order.entity.OrderItem;
import BridgeLabz.Book_Store_Application.order.mapper.OrderMapper;
import BridgeLabz.Book_Store_Application.order.repository.OrderItemRepository;
import BridgeLabz.Book_Store_Application.order.repository.OrderRepository;
import BridgeLabz.Book_Store_Application.order.service.OrderService;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;

    private final UserRepository userRepository;

    private final CustomerProfileRepository customerProfileRepository;

    private final AddressRepository addressRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        ));
    }

    private CustomerProfile getCustomerProfile(User user) {

        return customerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer profile not found."
                        ));
    }

    private Cart getCart(User user) {

        return cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found."
                        ));
    }

    private Address getShippingAddress(
            CustomerProfile customerProfile,
            Long addressId) {

        return addressRepository
                .findByIdAndCustomerProfile(addressId, customerProfile)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shipping address not found."
                        ));
    }

    @Override
    public OrderResponse placeOrder(
            Long userId,
            PlaceOrderRequest request) {

        // Get logged-in user
        User user = getUser(userId);

        // Get customer profile
        CustomerProfile customerProfile = getCustomerProfile(user);

        // Validate shipping address
        Address shippingAddress = getShippingAddress(
                customerProfile,
                request.getShippingAddressId()
        );

        // Get cart
        Cart cart = getCart(user);

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Validate stock & calculate total
        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new BadRequestException(
                        product.getTitle() + " is unavailable."
                );
            }

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for " + product.getTitle()
                );
            }

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subtotal);
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .customerProfile(customerProfile)
                .shippingAddress(shippingAddress)
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create order items
        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .subtotal(
                            product.getPrice().multiply(
                                    BigDecimal.valueOf(cartItem.getQuantity())
                            )
                    )
                    .build();

            orderItemRepository.save(orderItem);

            // Reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - cartItem.getQuantity()
            );

            productRepository.save(product);
        }

        // Clear cart
        cartItemRepository.deleteAll(cartItems);

        // Reload order with items
        Order finalOrder = orderRepository.findById(savedOrder.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        return orderMapper.toResponse(finalOrder);
    }
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {

        User user = getUser(userId);

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(orderMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(
            Long userId,
            Long orderId) {

        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        ));

        return orderMapper.toResponse(order);
    }
    @Override
    public OrderResponse cancelOrder(
            Long userId,
            Long orderId) {

        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        ));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException(
                    "Delivered orders cannot be cancelled."
            );
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException(
                    "Order is already cancelled."
            );
        }

        // Restore stock
        for (OrderItem orderItem : order.getOrderItems()) {

            Product product = orderItem.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity() + orderItem.getQuantity()
            );

            productRepository.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }
}