package BridgeLabz.Book_Store_Application.product.mapper;

import BridgeLabz.Book_Store_Application.category.entity.Category;
import BridgeLabz.Book_Store_Application.product.dto.ProductRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductUpdateRequest;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    /**
     * Convert ProductRequest -> Product Entity
     */
    public Product toEntity(ProductRequest request, Category category) {

        if (request == null) {
            return null;
        }

        return Product.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();
    }

    /**
     * Convert Product Entity -> ProductResponse
     */
    public ProductResponse toResponse(Product product) {

        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .author(product.getAuthor())
                .isbn(product.getIsbn())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )
                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory().getName()
                                : null
                )
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Update existing Product using ProductUpdateRequest
     */
    public void updateEntity(ProductUpdateRequest request,
                             Product product,
                             Category category) {

        if (request == null || product == null) {
            return;
        }

        product.setTitle(request.getTitle());
        product.setAuthor(request.getAuthor());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }

}