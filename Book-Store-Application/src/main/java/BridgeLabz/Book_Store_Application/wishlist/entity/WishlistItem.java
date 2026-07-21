package BridgeLabz.Book_Store_Application.wishlist.entity;

import BridgeLabz.Book_Store_Application.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wishlist_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"wishlist_id", "product_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many wishlist items belong to one wishlist.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    /**
     * Many wishlist items can reference one product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}