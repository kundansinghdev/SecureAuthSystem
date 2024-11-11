package com.secureauthsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.secureauthsystem.dto.ProductCategoryDTO;
import com.secureauthsystem.service.ProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;
    private final static Logger log = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Endpoint to get all categories
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        log.info("Request received to fetch all categories");
        try {
            List<ProductCategoryDTO> categoryList = categoryService.getAllCategories();
            log.info("Successfully fetched {} categories", categoryList.size());
            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            log.error("Error occurred while fetching categories: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
