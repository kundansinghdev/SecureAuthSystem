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
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final static Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO) {
        log.info("Signup request received: {}", userCredentialsDTO);
        String message = null;
        try {
            message = authService.signup(userCredentialsDTO);
            log.info("Signup successful: {}", message);
        } catch (Exception e) {
            log.error("Exception during signup", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("Signup response sent");
      // return ResponseEntity.status(HttpStatus.CREATED).body(message);
        return ResponseEntity.ok(message);
       
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCredentialsDTO loginCredentialsDTO) {
        log.info("Login request received for username: {}", loginCredentialsDTO.getUsername());
        String otpMessage = authService.loginByUsernameAndPassword(
                loginCredentialsDTO.getUsername(), 
                loginCredentialsDTO.getPassword()
        );

        if (otpMessage != null) {
            log.info("Login successful, OTP sent");
            return ResponseEntity.ok(otpMessage);
        } else {
            log.warn("Login failed: Invalid credentials");
            return ResponseEntity.badRequest().body(
                "Oops! We couldn't log you in with those details. Please double-check your username and password, and try again."
            );
        }
    }

    // Login with Mobile Number Endpoint
    @PostMapping("/login-with-mobile")
    public ResponseEntity<String> loginWithMobile(@Valid @RequestBody MobileLoginDTO mobileLoginDTO) {
        log.info("Login request with mobile received: {}", mobileLoginDTO.getMobileNumber());
        Optional<String> otpMessage = authService.loginWithMobile(mobileLoginDTO.getMobileNumber());

        if (otpMessage.isPresent()) {
            log.info("Login with mobile successful, OTP sent");
            return ResponseEntity.ok(otpMessage.get());
        } else {
            log.warn("Login with mobile failed: Mobile number not registered");
            return ResponseEntity.badRequest().body("This mobile number is not registered. Please sign up first.");
        }
    }

    // OTP Verification Endpoint for Mobile
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDTO otpVerificationDTO) {
        log.info("OTP verification request received for mobile: {}", otpVerificationDTO.getMobileNumber());
        boolean isVerified = authService.verifyOtp(otpVerificationDTO.getMobileNumber(), otpVerificationDTO.getOtp());

        if (isVerified) {
            log.info("OTP verified successfully for mobile: {}", otpVerificationDTO.getMobileNumber());
            return ResponseEntity.ok("OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            log.warn("OTP verification failed for mobile: {}", otpVerificationDTO.getMobileNumber());
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }

    // Login with Email Endpoint
    @PostMapping("/login-with-email")
    public ResponseEntity<String> loginWithEmail(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        log.info("Login request with email received: {}", emailLoginDTO.getEmail());
        Optional<String> otpMessage = authService.loginWithEmail(emailLoginDTO.getEmail());

        if (otpMessage.isPresent()) {
            log.info("Login with email successful, OTP sent");
            return ResponseEntity.ok(otpMessage.get());
        } else {
            log.warn("Login with email failed: Email not registered");
            return ResponseEntity.badRequest().body("This email is not registered. Please sign up first.");
        }
    }

    // OTP Verification Endpoint for Email
    @PostMapping("/verify-email-otp")
    public ResponseEntity<String> verifyEmailOtp(@Valid @RequestBody OtpVerificationEmailDTO otpVerificationEmailDTO) {
        log.info("Email OTP verification request received for email: {}", otpVerificationEmailDTO.getEmail());
        boolean isVerified = authService.verifyEmailOtp(otpVerificationEmailDTO.getEmail(), otpVerificationEmailDTO.getOtp());

        if (isVerified) {
            log.info("Email OTP verified successfully for email: {}", otpVerificationEmailDTO.getEmail());
            return ResponseEntity.ok("Email OTP verified successfully! You're now logged in. Welcome back!");
        } else {
            log.warn("Email OTP verification failed for email: {}", otpVerificationEmailDTO.getEmail());
            return ResponseEntity.badRequest().body("The OTP you entered is incorrect. Please try again.");
        }
    }

    // Forget Password with Email
    @PostMapping("/ForgetPasswordwithemail")
    public ResponseEntity<String> forgetpasswordWithEmail(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        log.info("Password reset request with email received: {}", emailLoginDTO.getEmail());
        Optional<String> otpMessage = authService.loginWithEmail(emailLoginDTO.getEmail());

        if (otpMessage.isPresent()) {
            log.info("Password reset OTP sent for email: {}", emailLoginDTO.getEmail());
            return ResponseEntity.ok(otpMessage.get());
        } else {
            log.warn("Password reset failed: Email not registered");
            return ResponseEntity.badRequest().body("This email is not registered. Please sign up first.");
        }
    }

    // Request password reset with email
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        log.info("Password reset request received for email: {}", emailLoginDTO.getEmail());
        Optional<String> otpMessage = authService.sendPasswordResetOtp(emailLoginDTO.getEmail());

        if (otpMessage.isPresent()) {
            log.info("Password reset OTP sent for email: {}", emailLoginDTO.getEmail());
            return ResponseEntity.ok(otpMessage.get());
        } else {
            log.warn("Password reset failed: Email not registered");
            return ResponseEntity.badRequest().body("Email not registered. Please sign up.");
        }
    }

    // Verify OTP and reset password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        log.info("Password reset OTP verification received for email: {}", passwordResetDTO.getEmail());
        boolean isReset = authService.verifyOtpAndResetPassword(
            passwordResetDTO.getEmail(), 
            passwordResetDTO.getOtp(), 
            passwordResetDTO.getNewPassword()
        );

        if (isReset) {
            log.info("Password reset successfully for email: {}", passwordResetDTO.getEmail());
            return ResponseEntity.ok("Password reset successfully! You can now log in with your new password.");
        } else {
            log.warn("Password reset failed: Invalid OTP or request expired");
            return ResponseEntity.badRequest().body("Invalid OTP or request expired. Please try again.");
        }
    }

    // Request username reset with email
    @PostMapping("/request-username-reset")
    public ResponseEntity<String> requestUsernameReset(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        log.info("Username reset request received for email: {}", emailLoginDTO.getEmail());
        Optional<String> otpMessage = authService.sendUsernameResetOtp(emailLoginDTO.getEmail());

        if (otpMessage.isPresent()) {
            log.info("Username reset OTP sent for email: {}", emailLoginDTO.getEmail());
            return ResponseEntity.ok(otpMessage.get());
        } else {
            log.warn("Username reset failed: Email not registered");
            return ResponseEntity.badRequest().body("Email not registered. Please sign up.");
        }
    }

    // Verify OTP and reset username
    @PostMapping("/reset-username")
    public ResponseEntity<String> resetUsername(@Valid @RequestBody UsernameResetDTO usernameResetDTO) {
        log.info("Username reset OTP verification received for email: {}", usernameResetDTO.getEmail());
        boolean isReset = authService.verifyOtpAndResetUsername(
            usernameResetDTO.getEmail(), 
            usernameResetDTO.getOtp(), 
            usernameResetDTO.getNewUsername()
        );

        if (isReset) {
            log.info("Username reset successfully for email: {}", usernameResetDTO.getEmail());
            return ResponseEntity.ok("Username reset successfully! You can now log in with your new username.");
        } else {
            log.warn("Username reset failed: Invalid OTP or request expired");
            return ResponseEntity.badRequest().body("Invalid OTP or request expired. Please try again.");
        }
    }
}
