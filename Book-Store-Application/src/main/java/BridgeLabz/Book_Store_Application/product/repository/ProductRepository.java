package BridgeLabz.Book_Store_Application.product.repository;

import BridgeLabz.Book_Store_Application.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    /**
     * Check whether ISBN already exists.
     */
    boolean existsByIsbn(String isbn);

    /**
     * Find product by ISBN.
     */
    Optional<Product> findByIsbn(String isbn);

    /**
     * Get all active products.
     */
    List<Product> findByActiveTrue();

    /**
     * Get active products by category.
     */
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    /**
     * Get active product by id.
     */
    Optional<Product> findByIdAndActiveTrue(Long id);

}