package com.secureauthsystem.service;

import com.secureauthsystem.dto.OtpVerificationEmailDTO;
import com.secureauthsystem.dto.UserCredentialsDTO;
import com.secureauthsystem.exception.UserAlreadyExistsException;
import com.secureauthsystem.model.UserCredentials;
import com.secureauthsystem.repository.UserCredentialsRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import com.secureauthsystem.config.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final TwilioConfig twilioConfig;
    private final EmailService emailService; // Injected to send emails

    // Store OTP data mapped to the email addresses
    private Map<String, OtpVerificationEmailDTO> otpStorage = new HashMap<>();

    @Autowired
    public AuthService(UserCredentialsRepository userCredentialsRepository, TwilioConfig twilioConfig, EmailService emailService) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.twilioConfig = twilioConfig;
        this.emailService = emailService; // Inject EmailService
    }

    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    // Method for user signup
    public String signup(UserCredentialsDTO userCredentialsDTO) {
        validateUserExistence(userCredentialsDTO);
        UserCredentials userCredentials = mapToUserCredentials(userCredentialsDTO);
        userCredentials.setPassword(userCredentialsDTO.getPassword()); // Store the plain-text password (should be hashed in production)
        userCredentialsRepository.save(userCredentials);

        // Send welcome email after successful signup
        emailService.sendWelcomeEmail(userCredentialsDTO.getEmail(), userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword(), userCredentialsDTO.getMobileNumber());

        return String.format("Congratulations, %s! Your account has been created successfully. Welcome aboard!", 
                             userCredentialsDTO.getUsername());
    }

    // Method for login using username and password
    public String loginByUsernameAndPassword(String username, String password) {
        Optional<UserCredentials> userCredentialsOpt = userCredentialsRepository.findByUsername(username);
        
        if (userCredentialsOpt.isEmpty() || !userCredentialsOpt.get().getPassword().equals(password)) {
            return null; // Indicate login failure
        }

        String mobileNumber = userCredentialsOpt.get().getMobileNumber();
        String formattedMobileNumber = formatToE164(mobileNumber);

        return sendOtp(formattedMobileNumber); // Send OTP and return the message
    }

    // Method for login using mobile number
    public Optional<String> loginWithMobile(String mobileNumber) {
        Optional<UserCredentials> user = userCredentialsRepository.findByMobileNumber(mobileNumber);

        if (user.isPresent()) {
            String formattedMobileNumber = formatToE164(mobileNumber);
            return Optional.of(sendOtp(formattedMobileNumber));
        } 
        return Optional.empty();
    }

    // Method to send OTP via SMS
    private String sendOtp(String mobileNumber) {
        try {
            Verification verification = Verification.creator(
                    twilioConfig.getServiceSid(),
                    mobileNumber,
                    "sms"
            ).create();

            return "An OTP has been sent to your mobile number. Please check and enter it to continue.";
        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            return "Oops! We couldn't send the OTP at this moment. Please try again later.";
        }
    }

    // Method for login using email
    public Optional<String> loginWithEmail(String email) {
        Optional<UserCredentials> user = userCredentialsRepository.findByEmail(email);

        if (user.isPresent()) {
            String otp = generateOtp();
            otpStorage.put(email, new OtpVerificationEmailDTO(email, otp));

            if (emailService.sendOtpToEmail(email, otp)) {
                return Optional.of("An OTP has been sent to your email address. Please check your inbox.");
            } else {
                return Optional.empty(); // Failed to send OTP
            }
        }
        return Optional.empty();
    }

    // Generate a random 6-digit OTP
    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(9999));
    }

    // Method to verify the OTP sent to mobile
    public boolean verifyOtp(String mobileNumber, String otp) {
        try {
            String formattedMobileNumber = formatToE164(mobileNumber);
            VerificationCheck verificationCheck = VerificationCheck.creator(twilioConfig.getServiceSid(), otp)
                    .setTo(formattedMobileNumber)
                    .create();

            return "approved".equals(verificationCheck.getStatus());
        } catch (Exception e) {
            System.err.println("Error verifying OTP: " + e.getMessage());
            return false;
        }
    }

    // New method to verify email OTP
    public boolean verifyEmailOtp(String email, String otp) {
        OtpVerificationEmailDTO otpData = otpStorage.get(email);

        if (otpData != null && otpData.getOtp().equals(otp)) {
            otpStorage.remove(email); // Remove OTP after successful verification
            return true; // OTP verified successfully
        }
        return false; // OTP verification failed
    }

    // Format mobile number to E.164 format
    private String formatToE164(String mobileNumber) {
        String countryCode = "+91"; // Update as needed
        return countryCode + mobileNumber;
    }

    private void validateUserExistence(UserCredentialsDTO userCredentialsDTO) {
        if (isUsernameTaken(userCredentialsDTO.getUsername()) ||
            isMobileNumberTaken(userCredentialsDTO.getMobileNumber())) {
            throw new UserAlreadyExistsException("A user with that username or mobile number already exists. Please try logging in or use different credentials.");
        }
    }

    // Check if the username is already taken
    private boolean isUsernameTaken(String username) {
        return userCredentialsRepository.findByUsername(username).isPresent();
    }

    // Check if the mobile number is already taken
    private boolean isMobileNumberTaken(String mobileNumber) {
        return userCredentialsRepository.findByMobileNumber(mobileNumber).isPresent();
    }

    // Map UserCredentialsDTO to UserCredentials model
    private UserCredentials mapToUserCredentials(UserCredentialsDTO userCredentialsDTO) {

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(userCredentialsDTO.getUsername());
        userCredentials.setPassword(userCredentialsDTO.getPassword()); // Ensure to hash in production
        userCredentials.setMobileNumber(userCredentialsDTO.getMobileNumber());
        userCredentials.setEmail(userCredentialsDTO.getEmail());
        return userCredentials;
    }
    
    // Method to send OTP for password reset
    public Optional<String> sendPasswordResetOtp(String email) {

        Optional<UserCredentials> user = userCredentialsRepository.findByEmail(email);

        if (user.isPresent()) {
            String otp = generateOtp();
            otpStorage.put(email, new OtpVerificationEmailDTO(email, otp));

            if (emailService.sendOtpToEmail(email, otp)) {
                return Optional.of("An OTP has been sent to your email for password reset.");
            }
        }
        return Optional.empty();
    }
    
    // Verify OTP and reset password
    public boolean verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        OtpVerificationEmailDTO otpData = otpStorage.get(email);

        if (otpData != null && otpData.getOtp().equals(otp)) {
            Optional<UserCredentials> user = userCredentialsRepository.findByEmail(email);

            if (user.isPresent()) {
                UserCredentials userCredentials = user.get();
                userCredentials.setPassword(newPassword); // In production, hash the password
                userCredentialsRepository.save(userCredentials);

                otpStorage.remove(email); // Clear OTP after successful reset
                return true;
            }
        }
        return false;
    }
    
    // Method to send OTP for Username reset
    public Optional<String> sendUsernameResetOtp(String email) {

        Optional<UserCredentials> user = userCredentialsRepository.findByEmail(email);

        if (user.isPresent()) {
            String otp = generateOtp();
            otpStorage.put(email, new OtpVerificationEmailDTO(email, otp));

            if (emailService.sendOtpToEmail(email, otp)) {
                return Optional.of("An OTP has been sent to your email for UserName reset.");
            }
        }
        return Optional.empty();
    }
    
    // Verify OTP and reset Username
    public boolean verifyOtpAndResetUsername(String email, String otp, String newUsername) {
        OtpVerificationEmailDTO otpData = otpStorage.get(email);

        if (otpData != null && otpData.getOtp().equals(otp)) {
            Optional<UserCredentials> user = userCredentialsRepository.findByEmail(email);

            if (user.isPresent()) {
                UserCredentials userCredentials = user.get();
                userCredentials.setUsername(newUsername); // 
                userCredentialsRepository.save(userCredentials);

                otpStorage.remove(email); 
                return true;
            }
        }
        return false;
    }
 
}
