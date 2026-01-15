package com.osen.osenshop.core.product.dtos;

public record CreateProductRequest(
        String name,
        String description,
        Double price,
        Integer stock,
        Boolean isActive,
        String imageUrl,
        Long categoryId
) {
}
