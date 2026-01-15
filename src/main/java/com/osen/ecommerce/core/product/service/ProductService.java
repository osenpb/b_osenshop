package com.osen.ecommerce.core.product.service;

import com.osen.ecommerce.core.product.dtos.CreateProductRequest;
import com.osen.ecommerce.core.product.dtos.UpdateProductRequest;
import com.osen.ecommerce.core.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(CreateProductRequest product);
    void deleteById(Long id);
    Product update(Long id, UpdateProductRequest product);
    Page<Product> findAllPageable(Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String productName);

}
