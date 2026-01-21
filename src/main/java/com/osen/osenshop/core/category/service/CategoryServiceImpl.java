package com.osen.osenshop.core.category.service;

import com.osen.osenshop.core.category.model.Category;
import com.osen.osenshop.core.category.repository.CategoryRepository;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
