package com.secureauthsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secureauthsystem.dto.ProductCategoryDTO;
import com.secureauthsystem.model.ProductCategory;
import com.secureauthsystem.repository.ProductCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Retrieve all categories
    public List<ProductCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new ProductCategoryDTO(category.getCategoryId(), category.getName()))
                .collect(Collectors.toList());
    }
}
