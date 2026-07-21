package BridgeLabz.Book_Store_Application.cart.service.impl;

import BridgeLabz.Book_Store_Application.cart.dto.AddToCartRequest;
import BridgeLabz.Book_Store_Application.cart.dto.CartResponse;
import BridgeLabz.Book_Store_Application.cart.dto.UpdateCartRequest;
import BridgeLabz.Book_Store_Application.cart.entity.Cart;
import BridgeLabz.Book_Store_Application.cart.entity.CartItem;
import BridgeLabz.Book_Store_Application.cart.mapper.CartMapper;
import BridgeLabz.Book_Store_Application.cart.repository.CartItemRepository;
import BridgeLabz.Book_Store_Application.cart.repository.CartRepository;
import BridgeLabz.Book_Store_Application.cart.service.CartService;
import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    private Cart getOrCreateCart(User user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart cart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(cart);
                });
    }

    private void calculateCartTotal(Cart cart) {

        BigDecimal total = cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
    }

    @Override
    public CartResponse addToCart(Long userId, AddToCartRequest request) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find or create cart
        Cart cart = getOrCreateCart(user);

        // Find product
        Product product = productRepository.findByIdAndActiveTrue(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + request.getProductId()));

        // Validate stock
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new BadRequestException("Insufficient stock available.");
        }

        // Check whether product already exists in cart
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {

            int updatedQuantity = cartItem.getQuantity() + request.getQuantity();

            if (updatedQuantity > product.getStockQuantity()) {
                throw new BadRequestException("Insufficient stock available.");
            }

            cartItem.setQuantity(updatedQuantity);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setTotalPrice(
                    product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity))
            );

        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(
                            product.getPrice().multiply(
                                    BigDecimal.valueOf(request.getQuantity())
                            )
                    )
                    .build();

            cart.getCartItems().add(cartItem);
        }

        // Save cart item
        cartItemRepository.save(cartItem);

        // Calculate total amount
        calculateCartTotal(cart);

        // Save cart
        cartRepository.save(cart);

        // Return response
        return cartMapper.toResponse(cart);
    }
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        calculateCartTotal(cart);

        return cartMapper.toResponse(cart);
    }
    @Override
    public CartResponse updateCartItem(Long userId, UpdateCartRequest request) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        // Find product
        Product product = productRepository.findByIdAndActiveTrue(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + request.getProductId()));

        // Find cart item
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product is not available in the cart."));

        // Validate stock
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new BadRequestException("Insufficient stock available.");
        }

        // Update quantity
        cartItem.setQuantity(request.getQuantity());

        // Update latest price
        cartItem.setUnitPrice(product.getPrice());

        // Update total price
        cartItem.setTotalPrice(
                product.getPrice().multiply(
                        BigDecimal.valueOf(request.getQuantity())
                )
        );

        // Save cart item
        cartItemRepository.save(cartItem);

        // Recalculate cart total
        calculateCartTotal(cart);

        // Save cart
        cartRepository.save(cart);

        // Return updated cart
        return cartMapper.toResponse(cart);
    }
    @Override
    public CartResponse removeFromCart(Long userId, Long productId) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        // Find product
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + productId));

        // Find cart item
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product is not available in the cart."));

        // Remove cart item from cart
        cart.getCartItems().remove(cartItem);

        // Delete cart item
        cartItemRepository.delete(cartItem);

        // Recalculate cart total
        calculateCartTotal(cart);

        // Save cart
        cartRepository.save(cart);

        // Return updated cart
        return cartMapper.toResponse(cart);
    }
    @Override
    public void clearCart(Long userId) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        // Remove all cart items
        cart.getCartItems().clear();

        // Reset total amount
        cart.setTotalAmount(BigDecimal.ZERO);

        // Save cart
        cartRepository.save(cart);
    }

}