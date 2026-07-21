package BridgeLabz.Book_Store_Application.wishlist.repository;

import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.wishlist.entity.Wishlist;
import BridgeLabz.Book_Store_Application.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    /**
     * Find wishlist item by wishlist and product.
     */
    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);

    /**
     * Get all wishlist items.
     */
    List<WishlistItem> findByWishlist(Wishlist wishlist);

    /**
     * Check whether product already exists in wishlist.
     */
    boolean existsByWishlistAndProduct(Wishlist wishlist, Product product);

    /**
     * Remove one product from wishlist.
     */
    void deleteByWishlistAndProduct(Wishlist wishlist, Product product);

    /**
     * Remove all products from wishlist.
     */
    void deleteByWishlist(Wishlist wishlist);

}