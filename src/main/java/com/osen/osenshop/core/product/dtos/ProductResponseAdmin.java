package com.osen.osenshop.core.product.dtos;

import com.osen.osenshop.core.category.dto.CategoryResponse;

import java.time.LocalDate;

public record ProductResponseAdmin(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        CategoryResponse category,
        Boolean isActive,
        LocalDate createdAt
) {

}
