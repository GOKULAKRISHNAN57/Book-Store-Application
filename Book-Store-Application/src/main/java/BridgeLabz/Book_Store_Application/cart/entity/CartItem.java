package BridgeLabz.Book_Store_Application.cart.entity;

import BridgeLabz.Book_Store_Application.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many cart items belong to one cart.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Many cart items can reference one product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Quantity selected by the user.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Product price at the time it was added to the cart.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total price = quantity × unitPrice.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
}