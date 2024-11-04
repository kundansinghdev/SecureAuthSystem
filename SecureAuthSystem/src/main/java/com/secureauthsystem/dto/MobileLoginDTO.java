package com.secureauthsystem.dto;

import jakarta.validation.constraints.NotBlank;

// DTO for mobile login credentials with validation
public class MobileLoginDTO {
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    // Getters and Setters
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
