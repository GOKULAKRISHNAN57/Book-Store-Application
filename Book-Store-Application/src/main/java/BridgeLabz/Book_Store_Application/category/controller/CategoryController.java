package BridgeLabz.Book_Store_Application.category.controller;

import BridgeLabz.Book_Store_Application.category.dto.CategoryRequest;
import BridgeLabz.Book_Store_Application.category.dto.CategoryResponse;
import BridgeLabz.Book_Store_Application.category.service.CategoryService;
import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create Category
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.createCategory(request);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category created successfully")
                .data(response)
                .build();
    }

    /**
     * Get Category By Id
     */
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(
            @PathVariable Long id) {

        CategoryResponse response = categoryService.getCategoryById(id);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category fetched successfully")
                .data(response)
                .build();
    }

    /**
     * Get All Categories
     */
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {

        List<CategoryResponse> response =
                categoryService.getAllCategories();

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(response)
                .build();
    }

    /**
     * Update Category
     */
    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response =
                categoryService.updateCategory(id, request);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category updated successfully")
                .data(response)
                .build();
    }

    /**
     * Delete Category (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Category deleted successfully")
                .data("Category has been deactivated")
                .build();
    }
}