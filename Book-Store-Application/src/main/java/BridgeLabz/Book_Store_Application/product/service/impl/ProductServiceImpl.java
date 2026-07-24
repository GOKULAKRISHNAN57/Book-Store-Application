package BridgeLabz.Book_Store_Application.product.service.impl;

import BridgeLabz.Book_Store_Application.category.entity.Category;
import BridgeLabz.Book_Store_Application.category.repository.CategoryRepository;
import BridgeLabz.Book_Store_Application.common.constants.SortConstants;
import BridgeLabz.Book_Store_Application.common.util.SortUtil;
import BridgeLabz.Book_Store_Application.exception.DuplicateResourceException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.product.dto.ProductRequest;
import BridgeLabz.Book_Store_Application.product.dto.ProductResponse;
import BridgeLabz.Book_Store_Application.product.dto.ProductUpdateRequest;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.mapper.ProductMapper;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import BridgeLabz.Book_Store_Application.product.dto.ProductFilterRequest;
import BridgeLabz.Book_Store_Application.product.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    /**
     * Create Product
     */
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        log.info("Creating product : {}", request.getTitle());

        if (productRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException(
                    "Product with ISBN already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : "
                                        + request.getCategoryId()));

        Product product = productMapper.toEntity(request, category);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    /**
     * Update Product
     */
    @Override
    public ProductResponse updateProduct(Long id,
                                         ProductUpdateRequest request) {

        log.info("Updating product with id : {}", id);

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : "
                                        + request.getCategoryId()));

        productMapper.updateEntity(request, product, category);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Get Product By ID
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {

        log.info("Fetching product with id : {}", id);

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        return productMapper.toResponse(product);
    }



    /**
     * Get Products By Category
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        log.info("Fetching products of category {}", categoryId);

        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId));

        return productRepository
                .findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    /**
     * Soft Delete Product
     */
    @Override
    public void deleteProduct(Long id) {

        log.info("Deleting product {}", id);

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        product.setActive(false);

        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(ProductFilterRequest request) {

        log.info("Fetching products with filters");

        Sort sort = SortUtil.getSort(
                request.getSortBy(),
                request.getDirection(),
                SortConstants.PRODUCT_SORT_FIELDS
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        Specification<Product> specification =
                ProductSpecification.buildSpecification(request);

        Page<Product> products = productRepository.findAll(
                specification,
                pageable
        );

        return products.map(productMapper::toResponse);
    }

}