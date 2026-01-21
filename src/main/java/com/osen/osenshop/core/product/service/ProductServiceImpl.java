package com.osen.osenshop.core.product.service;

import com.osen.osenshop.core.category.model.Category;
import com.osen.osenshop.core.category.service.CategoryService;
import com.osen.osenshop.core.product.dtos.CreateProductRequest;
import com.osen.osenshop.core.product.dtos.UpdateProductRequest;
import com.osen.osenshop.core.product.model.Product;
import com.osen.osenshop.core.product.repository.ProductRepository;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

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
    @Transactional
    public Product save(CreateProductRequest productRequest) {

        if (productRequest.categoryId() == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio");
        }

        Category category = categoryService.findById(productRequest.categoryId());
        log.info("Categoria encontrada {}", category.getName());
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
        log.info("Producto creado");
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
