package BridgeLabz.Book_Store_Application.product.repository;

import BridgeLabz.Book_Store_Application.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByIsbn(String isbn);

    Optional<Product> findByIsbn(String isbn);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByTitleContainingIgnoreCaseAndActiveTrue(String title);

}