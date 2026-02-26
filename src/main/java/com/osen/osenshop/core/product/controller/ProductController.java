package com.osen.osenshop.core.product.controller;

import com.osen.osenshop.core.category.service.CategoryService;
import com.osen.osenshop.core.product.dtos.CreateProductRequest;
import com.osen.osenshop.core.product.dtos.ProductResponse;
import com.osen.osenshop.core.product.dtos.UpdateProductRequest;
import com.osen.osenshop.core.product.mapper.ProductMapper;
import com.osen.osenshop.core.product.model.Product;
import com.osen.osenshop.core.product.service.ProductService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService){
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {

        Product product = productService.findById(id);
        ProductResponse productResponse = ProductMapper.toDto(product);
        return ResponseEntity.ok(productResponse);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody CreateProductRequest productRequest) {
        productService.save(productRequest);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest productRequest) throws jakarta.persistence.EntityNotFoundException {
        Product updatedProducto = productService.update(id, productRequest);
        ProductResponse productResponse = ProductMapper.toDto(updatedProducto);
        return ResponseEntity.ok(productResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        List<ProductResponse> productResponseList = productService.findAll().stream().map(ProductMapper::toDto).toList();
        return ResponseEntity.ok(productResponseList);
    }


    @GetMapping("/pageable")
    public ResponseEntity<Page<ProductResponse>> getAllProductsPageable(@PageableDefault(size = 9) Pageable pageable) {

        Page<ProductResponse> page =
                productService.findAllPageable(pageable)
                        .map(ProductMapper::toDto);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ProductResponse>> getByName(
            @RequestParam String name
    ) {
        List<Product> products = productService.findByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<ProductResponse> response = products.stream()
                .map(ProductMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }



}
