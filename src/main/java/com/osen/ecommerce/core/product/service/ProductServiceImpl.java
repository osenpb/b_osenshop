package com.osen.ecommerce.core.product.service;

import com.osen.ecommerce.core.category.model.Category;
import com.osen.ecommerce.core.category.service.CategoryService;
import com.osen.ecommerce.core.product.dtos.CreateProductRequest;
import com.osen.ecommerce.core.product.dtos.UpdateProductRequest;
import com.osen.ecommerce.core.product.model.Product;
import com.osen.ecommerce.core.product.repository.ProductRepository;
import com.osen.ecommerce.common.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }
    @Override
    public Product save(CreateProductRequest productRequest) {

        Category category = categoryService.findById(productRequest.categoryId());
        Product product = new Product(
                null,
                productRequest.name(),
                productRequest.description(),
                productRequest.price(),
                productRequest.stock(),
                productRequest.imageUrl(),
                LocalDateTime.now(),
                true,
                category
        );

        return productRepository.save(product);
    }
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Product update(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Producto no encontrado"));

        Category category = categoryService.findById(request.categoryId());

        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl());
        product.setDescription(request.description());
        product.setIsActive(request.isActive());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Page<Product> findAllPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findByNameContainingIgnoreCase(String productName) {
        return productRepository.findByNameContainingIgnoreCase(productName);
    }
}
