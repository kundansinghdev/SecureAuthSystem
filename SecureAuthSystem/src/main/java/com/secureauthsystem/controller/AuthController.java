package com.secureauthsystem.controller;

import com.secureauthsystem.dto.*;
import com.secureauthsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/auth") // Base URL for authentication endpoints
public class AuthController {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO) {
        log.info("Signup request received for user: {}", userCredentialsDTO);
        try {
            String message = authService.signup(userCredentialsDTO);
            log.info("Signup successful for user: {}", userCredentialsDTO.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (RuntimeException e) {
            log.error("Signup failed for user: {}. Error: {}", userCredentialsDTO.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCredentialsDTO loginCredentialsDTO) {
        log.info("Login request received for user: {}", loginCredentialsDTO.getUsername());
        try {
            String otpMessage = authService.loginByUsernameAndPassword(
                    loginCredentialsDTO.getUsername(), 
                    loginCredentialsDTO.getPassword()
            );
            if (otpMessage != null) {
                log.info("Login successful, OTP sent to user: {}", loginCredentialsDTO.getUsername());
                return ResponseEntity.ok(otpMessage);
            } else {
                log.warn("Login failed for user: {}", loginCredentialsDTO.getUsername());
                return ResponseEntity.badRequest().body(
                        "Oops! We couldn't log you in with those details. Please double-check your username and password, and try again."
                );
            }
        } catch (Exception e) {
            log.error("Login error for user: {}. Error: {}", loginCredentialsDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Login with Mobile Number Endpoint
    @PostMapping("/login-with-mobile")
    public ResponseEntity<String> loginWithMobile(@Valid @RequestBody MobileLoginDTO mobileLoginDTO) {
        log.info("Login with mobile request received for mobile number: {}", mobileLoginDTO.getMobileNumber());
        try {
            Optional<String> otpMessage = authService.loginWithMobile(mobileLoginDTO.getMobileNumber());

            if (otpMessage.isPresent()) {
                log.info("Login successful, OTP sent to mobile number: {}", mobileLoginDTO.getMobileNumber());
                return ResponseEntity.ok(otpMessage.get());
            } else {
                log.warn("Login with mobile failed. Mobile number not registered: {}", mobileLoginDTO.getMobileNumber());
                return ResponseEntity.badRequest().body("This mobile number is not registered. Please sign up first.");
            }
        } catch (Exception e) {
            log.error("Error during login with mobile for number: {}. Error: {}", mobileLoginDTO.getMobileNumber(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // OTP Verification Endpoint for Mobile
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDTO otpVerificationDTO) {
        log.info("OTP verification request received for mobile number: {}", otpVerificationDTO.getMobileNumber());
        boolean isVerified = authService.verifyOtp(otpVerificationDTO.getMobileNumber(), otpVerificationDTO.getOtp());

        if (isVerified) {
            log.info("OTP verification successful for mobile number: {}", otpVerificationDTO.getMobileNumber());
            return ResponseEntity.ok("OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            log.warn("OTP verification failed for mobile number: {}", otpVerificationDTO.getMobileNumber());
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }

    // Login with Email Endpoint
    @PostMapping("/login-with-email")
    public ResponseEntity<String> loginWithEmail(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        log.info("Login with email request received for email: {}", emailLoginDTO.getEmail());
        try {
            Optional<String> otpMessage = authService.loginWithEmail(emailLoginDTO.getEmail());

            if (otpMessage.isPresent()) {
                log.info("Login successful, OTP sent to email: {}", emailLoginDTO.getEmail());
                return ResponseEntity.ok(otpMessage.get());
            } else {
                log.warn("Login with email failed. Email not registered: {}", emailLoginDTO.getEmail());
                return ResponseEntity.badRequest().body("This email is not registered. Please sign up first.");
            }
        } catch (Exception e) {
            log.error("Error during login with email for email: {}. Error: {}", emailLoginDTO.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // OTP Verification Endpoint for Email
    @PostMapping("/verify-email-otp")
    public ResponseEntity<String> verifyEmailOtp(@Valid @RequestBody OtpVerificationEmailDTO otpVerificationEmailDTO) {
        log.info("Email OTP verification request received for email: {}", otpVerificationEmailDTO.getEmail());
        boolean isVerified = authService.verifyEmailOtp(otpVerificationEmailDTO.getEmail(), otpVerificationEmailDTO.getOtp());

        if (isVerified) {
            log.info("Email OTP verification successful for email: {}", otpVerificationEmailDTO.getEmail());
            return ResponseEntity.ok("Email OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            log.warn("Email OTP verification failed for email: {}", otpVerificationEmailDTO.getEmail());
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }
}
