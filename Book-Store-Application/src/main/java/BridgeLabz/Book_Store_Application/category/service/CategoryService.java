package BridgeLabz.Book_Store_Application.category.service;

import BridgeLabz.Book_Store_Application.category.dto.CategoryRequest;
import BridgeLabz.Book_Store_Application.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long id);

}