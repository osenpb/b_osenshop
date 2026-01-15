package com.osen.osenshop.core.category.repository;

import com.osen.osenshop.core.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
