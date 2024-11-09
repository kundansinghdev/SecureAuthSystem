package com.secureauthsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.secureauthsystem.model.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    // Custom query to find all categories
    List<ProductCategory> findAll();
}
