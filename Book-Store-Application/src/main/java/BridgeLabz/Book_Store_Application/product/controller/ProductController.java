package BridgeLabz.Book_Store_Application.product.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductFilterRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductUpdateRequest;
import BridgeLabz.Book_Store_Application.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Create Product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product created successfully")
                .data(response)
                .build();
    }

    /**
     * Get Product By Id
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable Long id) {

        ProductResponse response = productService.getProductById(id);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(response)
                .build();
    }

    /**
     * Get Products
     * Supports:
     * Pagination
     * Sorting
     * Search
     * Filtering
     */
    @GetMapping
    public ApiResponse<Page<ProductResponse>> getProducts(
            ProductFilterRequest request) {

        Page<ProductResponse> response =
                productService.getProducts(request);

        return ApiResponse.success(
                "Products retrieved successfully",
                response
        );
    }

    /**
     * Get Products By Category
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductResponse> response =
                productService.getProductsByCategory(categoryId);

        return ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(response)
                .build();
    }

    /**
     * Update Product
     */
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {

        ProductResponse response =
                productService.updateProduct(id, request);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product updated successfully")
                .data(response)
                .build();
    }

    /**
     * Delete Product
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Product deleted successfully")
                .data("Product has been deactivated")
                .build();
    }
}