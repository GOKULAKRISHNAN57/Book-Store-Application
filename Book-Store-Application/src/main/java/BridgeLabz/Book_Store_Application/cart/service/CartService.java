package BridgeLabz.Book_Store_Application.cart.service;

import BridgeLabz.Book_Store_Application.cart.dto.AddToCartRequest;
import BridgeLabz.Book_Store_Application.cart.dto.CartResponse;
import BridgeLabz.Book_Store_Application.cart.dto.UpdateCartRequest;

public interface CartService {

    /**
     * Add a product to the user's cart.
     *
     * @param userId Logged-in user's ID
     * @param request Product and quantity
     * @return Updated cart
     */
    CartResponse addToCart(Long userId, AddToCartRequest request);

    /**
     * Get the logged-in user's cart.
     *
     * @param userId Logged-in user's ID
     * @return Cart details
     */
    CartResponse getCart(Long userId);

    /**
     * Update the quantity of a product in the cart.
     *
     * @param userId Logged-in user's ID
     * @param request Product and new quantity
     * @return Updated cart
     */
    CartResponse updateCartItem(Long userId, UpdateCartRequest request);

    /**
     * Remove a product from the cart.
     *
     * @param userId Logged-in user's ID
     * @param productId Product ID
     * @return Updated cart
     */
    CartResponse removeFromCart(Long userId, Long productId);

    /**
     * Remove all products from the cart.
     *
     * @param userId Logged-in user's ID
     */
    void clearCart(Long userId);
}