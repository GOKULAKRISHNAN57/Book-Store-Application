package BridgeLabz.Book_Store_Application.category.mapper;

import BridgeLabz.Book_Store_Application.category.dto.CategoryRequest;
import BridgeLabz.Book_Store_Application.category.dto.CategoryResponse;
import BridgeLabz.Book_Store_Application.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    /**
     * Convert CategoryRequest to Category Entity
     */
    public Category toEntity(CategoryRequest request) {

        if (request == null) {
            return null;
        }

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    /**
     * Convert Category Entity to CategoryResponse
     */
    public CategoryResponse toResponse(Category category) {

        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    /**
     * Update Existing Category Entity
     */
    public void updateEntity(CategoryRequest request, Category category) {

        if (request == null || category == null) {
            return;
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }

}