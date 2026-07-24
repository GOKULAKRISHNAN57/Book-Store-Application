package BridgeLabz.Book_Store_Application.product.service;

import BridgeLabz.Book_Store_Application.product.dto.ProductFilterRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;

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
     * Get products with pagination, sorting, searching and filtering.
     */
    Page<ProductResponse> getProducts(ProductFilterRequest request);

    /**
     * Get all products by category.
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    /**
     * Soft delete a product.
     */
    void deleteProduct(Long id);

}