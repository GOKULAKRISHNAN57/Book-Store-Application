package BridgeLabz.Book_Store_Application.cart.controller;

import BridgeLabz.Book_Store_Application.cart.dto.AddToCartRequest;
import BridgeLabz.Book_Store_Application.cart.dto.CartResponse;
import BridgeLabz.Book_Store_Application.cart.dto.UpdateCartRequest;
import BridgeLabz.Book_Store_Application.cart.service.CartService;
import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Add product to cart.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddToCartRequest request) {

        CartResponse response = cartService.addToCart(userDetails.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Product added to cart successfully.",
                        response
                ));
    }

    /**
     * View cart.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CartResponse response = cartService.getCart(userDetails.getId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart fetched successfully.",
                        response
                )
        );
    }

    /**
     * Update cart item quantity.
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateCartRequest request) {

        CartResponse response =
                cartService.updateCartItem(userDetails.getId(), request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart updated successfully.",
                        response
                )
        );
    }

    /**
     * Remove product from cart.
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {

        CartResponse response =
                cartService.removeFromCart(userDetails.getId(), productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product removed from cart successfully.",
                        response
                )
        );
    }

    /**
     * Clear cart.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.clearCart(userDetails.getId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart cleared successfully.",
                        null
                )
        );
    }
}