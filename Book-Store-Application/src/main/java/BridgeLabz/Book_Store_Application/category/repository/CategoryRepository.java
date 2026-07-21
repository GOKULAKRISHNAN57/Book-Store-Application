package BridgeLabz.Book_Store_Application.category.repository;

import BridgeLabz.Book_Store_Application.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Check whether a category name already exists.
     */
    boolean existsByName(String name);

    /**
     * Find category by name.
     */
    Optional<Category> findByName(String name);

    /**
     * Get all active categories.
     */
    List<Category> findByActiveTrue();

}