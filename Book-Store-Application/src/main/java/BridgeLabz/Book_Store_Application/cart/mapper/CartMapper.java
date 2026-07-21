package BridgeLabz.Book_Store_Application.cart.mapper;

import BridgeLabz.Book_Store_Application.cart.dto.CartItemResponse;
import BridgeLabz.Book_Store_Application.cart.dto.CartResponse;
import BridgeLabz.Book_Store_Application.cart.entity.Cart;
import BridgeLabz.Book_Store_Application.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    /**
     * Convert Cart entity to CartResponse DTO.
     */
    public CartResponse toResponse(Cart cart) {

        List<CartItemResponse> itemResponses = cart.getCartItems()
                .stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemResponses)
                .totalAmount(cart.getTotalAmount())
                .build();
    }

    /**
     * Convert CartItem entity to CartItemResponse DTO.
     */
    public CartItemResponse toCartItemResponse(CartItem cartItem) {

        return CartItemResponse.builder()
                .productId(cartItem.getProduct().getId())
                .productTitle(cartItem.getProduct().getTitle())
                .productAuthor(cartItem.getProduct().getAuthor())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}