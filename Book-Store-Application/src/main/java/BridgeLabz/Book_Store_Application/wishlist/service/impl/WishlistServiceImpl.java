package BridgeLabz.Book_Store_Application.wishlist.service.impl;

import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import BridgeLabz.Book_Store_Application.wishlist.dto.AddToWishlistRequest;
import BridgeLabz.Book_Store_Application.wishlist.dto.WishlistResponse;
import BridgeLabz.Book_Store_Application.wishlist.entity.Wishlist;
import BridgeLabz.Book_Store_Application.wishlist.entity.WishlistItem;
import BridgeLabz.Book_Store_Application.wishlist.mapper.WishlistMapper;
import BridgeLabz.Book_Store_Application.wishlist.repository.WishlistItemRepository;
import BridgeLabz.Book_Store_Application.wishlist.repository.WishlistRepository;
import BridgeLabz.Book_Store_Application.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishlistMapper wishlistMapper;


    private Wishlist getOrCreateWishlist(User user) {

        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {

                    Wishlist wishlist = Wishlist.builder()
                            .user(user)
                            .build();

                    return wishlistRepository.save(wishlist);
                });
    }
    @Override
    public WishlistResponse addToWishlist(Long userId, AddToWishlistRequest request) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find or create wishlist
        Wishlist wishlist = getOrCreateWishlist(user);

        // Find active product
        Product product = productRepository.findByIdAndActiveTrue(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + request.getProductId()));

        // Check if product already exists in wishlist
        if (wishlistItemRepository.existsByWishlistAndProduct(wishlist, product)) {
            throw new BadRequestException("Product already exists in wishlist.");
        }

        // Create wishlist item
        WishlistItem wishlistItem = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .build();

        // Maintain bidirectional relationship
        wishlist.getWishlistItems().add(wishlistItem);

        // Save wishlist item
        wishlistItemRepository.save(wishlistItem);

        // Return updated wishlist
        return wishlistMapper.toResponse(wishlist);
    }
    @Override
    @Transactional(readOnly = true)
    public WishlistResponse getWishlist(Long userId) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist not found."));

        // Convert entity to DTO
        return wishlistMapper.toResponse(wishlist);
    }
    @Override
    public WishlistResponse removeFromWishlist(Long userId, Long productId) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist not found."));

        // Find active product
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + productId));

        // Find wishlist item
        WishlistItem wishlistItem = wishlistItemRepository
                .findByWishlistAndProduct(wishlist, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found in wishlist."));

        // Remove from bidirectional relationship
        wishlist.getWishlistItems().remove(wishlistItem);

        // Delete wishlist item
        wishlistItemRepository.delete(wishlistItem);

        // Return updated wishlist
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    public void clearWishlist(Long userId) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find user's wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist not found."));

        // Remove all items from the collection
        wishlist.getWishlistItems().clear();

        // Save wishlist (orphanRemoval will delete child records)
        wishlistRepository.save(wishlist);
    }

}