package com.osen.osenshop.core.product.service;

import com.osen.osenshop.core.product.dtos.CreateProductRequest;
import com.osen.osenshop.core.product.dtos.UpdateProductRequest;
import com.osen.osenshop.core.product.model.Product;
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
