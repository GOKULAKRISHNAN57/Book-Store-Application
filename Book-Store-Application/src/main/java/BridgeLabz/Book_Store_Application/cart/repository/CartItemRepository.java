package BridgeLabz.Book_Store_Application.cart.repository;

import BridgeLabz.Book_Store_Application.cart.entity.Cart;
import BridgeLabz.Book_Store_Application.cart.entity.CartItem;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Find cart item by cart and product.
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    /**
     * Get all cart items of a cart.
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * Remove one product from cart.
     */
    void deleteByCartAndProduct(Cart cart, Product product);

    /**
     * Remove all items from a cart.
     */
    void deleteByCart(Cart cart);

    /**
     * Check whether product already exists in cart.
     */
    boolean existsByCartAndProduct(Cart cart, Product product);

}