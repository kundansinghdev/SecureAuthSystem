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

// Controller for handling requests related to Product Categories
@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    // Injected service to handle business logic for product categories
    private final ProductCategoryService categoryService;
    
    // Logger to log messages for monitoring and debugging
    private final static Logger log = LoggerFactory.getLogger(ProductCategoryController.class);

    // Constructor-based dependency injection for ProductCategoryService
    @Autowired
    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * GET endpoint to retrieve all product categories
     * @return ResponseEntity containing a list of ProductCategoryDTO objects if successful, or a 500 status if an error occurs
     */
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        log.info("Request received to fetch all categories");
        
        try {
            // Retrieve all categories using the service
            List<ProductCategoryDTO> categoryList = categoryService.getAllCategories();
            
            // Log the number of categories retrieved
            log.info("Successfully fetched {} categories", categoryList.size());
            
            // Return the list wrapped in a 200 OK response
            return ResponseEntity.ok(categoryList);
            
        } catch (Exception e) {
            // Log any errors that occur during retrieval
            log.error("Error occurred while fetching categories: {}", e.getMessage());
            
            // Return a 500 Internal Server Error response if an error occurs
            return ResponseEntity.status(500).body(null);
        }
    }
}
