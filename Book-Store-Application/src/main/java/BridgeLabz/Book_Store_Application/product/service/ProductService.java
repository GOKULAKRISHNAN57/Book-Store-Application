package BridgeLabz.Book_Store_Application.product.service;

import BridgeLabz.Book_Store_Application.product.dto.ProductRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    /**
     * Create a new product.
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Update an existing product.
     */
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    /**
     * Get product by ID.
     */
    ProductResponse getProductById(Long id);

    /**
     * Get all active products.
     */
    List<ProductResponse> getAllProducts();

    /**
     * Get all products by category.
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    /**
     * Search products by title.
     */
    List<ProductResponse> searchProducts(String keyword);

    /**
     * Soft delete a product.
     */
    void deleteProduct(Long id);
}