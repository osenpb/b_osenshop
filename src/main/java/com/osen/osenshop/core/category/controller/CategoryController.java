package com.osen.osenshop.core.category.controller;

import com.osen.osenshop.core.category.dto.CategoryResponse;
import com.osen.osenshop.core.category.mapper.CategoryMapper;
import com.osen.osenshop.core.category.model.Category;
import com.osen.osenshop.core.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<?> categories() {
        List<Category> categoryList = categoryService.findAll();
        List<CategoryResponse> categoryResponseList =CategoryMapper.toListDto(categoryList);
        return ResponseEntity.ok(categoryResponseList);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id) {
        return null;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return null;
    }
}
