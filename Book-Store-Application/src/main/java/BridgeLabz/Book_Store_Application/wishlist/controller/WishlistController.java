package BridgeLabz.Book_Store_Application.wishlist.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import BridgeLabz.Book_Store_Application.wishlist.dto.AddToWishlistRequest;
import BridgeLabz.Book_Store_Application.wishlist.dto.WishlistResponse;
import BridgeLabz.Book_Store_Application.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Add product to wishlist.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<WishlistResponse>> addToWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddToWishlistRequest request) {

        WishlistResponse response = wishlistService.addToWishlist(
                userDetails.getId(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Product added to wishlist successfully.",
                        response
                ));
    }

    /**
     * Get user's wishlist.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<WishlistResponse>> getWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        WishlistResponse response = wishlistService.getWishlist(
                userDetails.getId()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wishlist retrieved successfully.",
                        response
                )
        );
    }

    /**
     * Remove product from wishlist.
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponse>> removeFromWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {

        WishlistResponse response = wishlistService.removeFromWishlist(
                userDetails.getId(),
                productId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product removed from wishlist successfully.",
                        response
                )
        );
    }

    /**
     * Clear wishlist.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        wishlistService.clearWishlist(userDetails.getId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wishlist cleared successfully.",
                        null
                )
        );
    }
}