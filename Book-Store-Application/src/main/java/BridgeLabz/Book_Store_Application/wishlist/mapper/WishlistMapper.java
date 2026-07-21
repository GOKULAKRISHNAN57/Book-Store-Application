package BridgeLabz.Book_Store_Application.wishlist.mapper;

import BridgeLabz.Book_Store_Application.wishlist.dto.WishlistItemResponse;
import BridgeLabz.Book_Store_Application.wishlist.dto.WishlistResponse;
import BridgeLabz.Book_Store_Application.wishlist.entity.Wishlist;
import BridgeLabz.Book_Store_Application.wishlist.entity.WishlistItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    /**
     * Convert Wishlist entity to WishlistResponse DTO.
     */
    public WishlistResponse toResponse(Wishlist wishlist) {

        List<WishlistItemResponse> itemResponses = wishlist.getWishlistItems()
                .stream()
                .map(this::toWishlistItemResponse)
                .collect(Collectors.toList());

        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .items(itemResponses)
                .build();
    }

    /**
     * Convert WishlistItem entity to WishlistItemResponse DTO.
     */
    public WishlistItemResponse toWishlistItemResponse(WishlistItem wishlistItem) {

        return WishlistItemResponse.builder()
                .productId(wishlistItem.getProduct().getId())
                .title(wishlistItem.getProduct().getTitle())
                .author(wishlistItem.getProduct().getAuthor())
                .imageUrl(wishlistItem.getProduct().getImageUrl())
                .price(wishlistItem.getProduct().getPrice())
                .build();
    }
}