package com.secureauthsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Represents the 'User_Credentials' table in the database
@Entity
@Table(name = "User_Credentials")
public class UserCredentials {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    // Username with validation and unique constraint
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    // Password with a minimum length constraint
    @NotBlank(message = "Password is required")
    @Size(min = 12, message = "Password must be at least 12 characters long")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // Mobile number with exact 10-digit format and unique constraint
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    @Column(name = "mobile_number", nullable = false, unique = true, length = 15)
    private String mobileNumber;

    // Email with format validation and max length
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    // Default constructor
    public UserCredentials() {}

    // Parameterized constructor to initialize all fields
    public UserCredentials(String username, String password, String mobileNumber, String email) {
        this.username = username;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
