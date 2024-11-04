package com.secureauthsystem.controller;

import com.secureauthsystem.dto.EmailLoginDTO;
import com.secureauthsystem.dto.LoginCredentialsDTO;
import com.secureauthsystem.dto.MobileLoginDTO;
import com.secureauthsystem.dto.OtpVerificationDTO;
import com.secureauthsystem.dto.OtpVerificationEmailDTO;
import com.secureauthsystem.dto.UserCredentialsDTO;
import com.secureauthsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth") // Base URL for authentication endpoints
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO) {
        try {
            String message = authService.signup(userCredentialsDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCredentialsDTO loginCredentialsDTO) {
        String username = loginCredentialsDTO.getUsername();
        String password = loginCredentialsDTO.getPassword();

        String otpMessage = authService.loginByUsernameAndPassword(username, password);
        if (otpMessage != null) {
            return ResponseEntity.ok(otpMessage); // Return the OTP message
        } else {
            return ResponseEntity.badRequest().body(
                "Oops! We couldn't log you in with those details. Please double-check your username and password, and try again."
            );
        }
    }

    // Login with Mobile Number Endpoint
    @PostMapping("/login-with-mobile")
    public ResponseEntity<String> loginWithMobile(@Valid @RequestBody MobileLoginDTO mobileLoginDTO) {
        String mobileNumber = mobileLoginDTO.getMobileNumber();

        // Fetch the OTP message if the mobile number exists
        Optional<String> otpMessage = authService.loginWithMobile(mobileNumber);

        if (otpMessage.isPresent()) {
            return ResponseEntity.ok(otpMessage.get()); // Return the OTP message
        } else {
            return ResponseEntity.badRequest().body("This mobile number is not registered. Please sign up first.");
        }
    }

    // OTP Verification Endpoint for Mobile
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDTO otpVerificationDTO) {
        boolean isVerified = authService.verifyOtp(otpVerificationDTO.getMobileNumber(), otpVerificationDTO.getOtp());

        if (isVerified) {
            return ResponseEntity.ok("OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }

    // Login with Email Endpoint
    @PostMapping("/login-with-email")
    public ResponseEntity<String> loginWithEmail(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail();

        // Fetch the OTP message if the email exists
        Optional<String> otpMessage = authService.loginWithEmail(email);

        if (otpMessage.isPresent()) {
            return ResponseEntity.ok(otpMessage.get()); // Return the OTP message
        } else {
            return ResponseEntity.badRequest().body("This email is not registered. Please sign up first.");
        }
    }

    // OTP Verification Endpoint for Email
    @PostMapping("/verify-email-otp")
    public ResponseEntity<String> verifyEmailOtp(@Valid @RequestBody OtpVerificationEmailDTO otpVerificationEmailDTO) {
        boolean isVerified = authService.verifyEmailOtp(otpVerificationEmailDTO.getEmail(), otpVerificationEmailDTO.getOtp());

        if (isVerified) {
            return ResponseEntity.ok("Email OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }
}
