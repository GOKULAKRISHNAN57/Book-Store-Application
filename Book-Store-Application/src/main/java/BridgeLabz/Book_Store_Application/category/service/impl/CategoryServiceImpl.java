package BridgeLabz.Book_Store_Application.category.service.impl;

import BridgeLabz.Book_Store_Application.category.dto.CategoryRequest;
import BridgeLabz.Book_Store_Application.category.dto.CategoryResponse;
import BridgeLabz.Book_Store_Application.category.entity.Category;
import BridgeLabz.Book_Store_Application.category.mapper.CategoryMapper;
import BridgeLabz.Book_Store_Application.category.repository.CategoryRepository;
import BridgeLabz.Book_Store_Application.category.service.CategoryService;
import BridgeLabz.Book_Store_Application.exception.DuplicateResourceException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        log.info("Creating category: {}", request.getName());

        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category already exists");
        }

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully: {}", savedCategory.getName());

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category already exists");
        }

        categoryMapper.updateEntity(request, category);

        Category updatedCategory = categoryRepository.save(category);

        log.info("Category updated successfully: {}", updatedCategory.getName());

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findByActiveTrue()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCategory(Long id) {

        log.info("Deleting category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        category.setActive(false);

        categoryRepository.save(category);

        log.info("Category deactivated successfully: {}", category.getName());
    }
}