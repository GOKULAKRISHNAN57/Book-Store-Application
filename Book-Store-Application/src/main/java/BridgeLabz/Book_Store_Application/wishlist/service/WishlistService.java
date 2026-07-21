package BridgeLabz.Book_Store_Application.wishlist.service;

import BridgeLabz.Book_Store_Application.wishlist.dto.AddToWishlistRequest;
import BridgeLabz.Book_Store_Application.wishlist.dto.WishlistResponse;

public interface WishlistService {

    /**
     * Add a product to the user's wishlist.
     *
     * @param userId Logged-in user's ID
     * @param request Product to add
     * @return Updated wishlist
     */
    WishlistResponse addToWishlist(Long userId, AddToWishlistRequest request);

    /**
     * Get the logged-in user's wishlist.
     *
     * @param userId Logged-in user's ID
     * @return Wishlist details
     */
    WishlistResponse getWishlist(Long userId);

    /**
     * Remove a product from the wishlist.
     *
     * @param userId Logged-in user's ID
     * @param productId Product ID
     * @return Updated wishlist
     */
    WishlistResponse removeFromWishlist(Long userId, Long productId);

    /**
     * Remove all products from the wishlist.
     *
     * @param userId Logged-in user's ID
     */
    void clearWishlist(Long userId);

}