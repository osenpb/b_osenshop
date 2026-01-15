package com.osen.osenshop.core.product.dtos;

import com.osen.osenshop.core.category.dto.CategoryResponse;


public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        CategoryResponse category,
        Boolean isActive
        //createdAt tampoco agrego por ahora
) {
}
