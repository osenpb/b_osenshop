package com.osen.osenshop.core.product.mapper;

import com.osen.osenshop.core.category.mapper.CategoryMapper;
import com.osen.osenshop.core.category.model.Category;
import com.osen.osenshop.core.category.service.CategoryService;
import com.osen.osenshop.core.product.dtos.CreateProductRequest;
import com.osen.osenshop.core.product.dtos.ProductResponse;
import com.osen.osenshop.core.product.dtos.UpdateProductRequest;
import com.osen.osenshop.core.product.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

    private final CategoryService categoryService;

    public ProductMapper(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public static ProductResponse toDto(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                CategoryMapper.toDto(p.getCategory()),
                p.getIsActive()
        );
    }

    public static Product toEntity(CreateProductRequest productRequest, Category category) {

        return Product.builder()
                .id(null)
                .name(productRequest.name())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .imageUrl(productRequest.imageUrl())
                .description(productRequest.description())
                .isActive(productRequest.isActive())
                .createdAt(LocalDateTime.now())
                .category(category)
                .build();

    }


    // NOTA: pongo void porque no se debe devolver una entidad, no debe devolver entidades un update xq genera bugs
    // y se pierde del contexto de persistencia del JPA
    public static void toEntity(UpdateProductRequest productRequest, Category category) {

        Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .imageUrl(productRequest.imageUrl())
                .description(productRequest.description())
                .isActive(productRequest.isActive())
                .createdAt(LocalDateTime.now())
                .category(category)
                .build();

    }
}
