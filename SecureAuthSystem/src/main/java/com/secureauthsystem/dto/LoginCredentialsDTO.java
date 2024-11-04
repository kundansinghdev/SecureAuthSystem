package com.secureauthsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO for login credentials with validation constraints
public class LoginCredentialsDTO {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can contain only letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 12, message = "Password must be at least 12 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{12,}$",
             message = "Password must have one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    // Getters and Setters
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
}
