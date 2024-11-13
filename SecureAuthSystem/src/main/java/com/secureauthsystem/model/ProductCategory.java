package com.secureauthsystem.model;

import jakarta.persistence.*;

// Represents the 'Category' table in the database
@Entity
@Table(name = "Category")
public class ProductCategory {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    // Name of the category, unique and non-nullable
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // Default constructor
    public ProductCategory() {}

    // Parameterized constructor to initialize fields
    public ProductCategory(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
