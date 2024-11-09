package com.secureauthsystem.service;

import com.secureauthsystem.dto.OtpVerificationEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Method to send a welcome email using ServiceSearch branding
    public void sendWelcomeEmail(String email, String username, String password, String mobileNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("🎉 Welcome to ServiceSearch! Your Account is Ready 🎉");

            message.setText("Hi " + username + ",\n\n" +
                            "👋 Welcome to ServiceSearch! We’re excited to have you with us. 😊\n\n" +

                            "🔑 Your Account Details:\n" +
                            "• 👤 Username: " + username + "\n" +
                            "• 📧 Email: " + email + "\n" +
                            "• 🔒 Password: " + password + " (please change it after your first login)\n" +
                            "• 📱 Mobile: " + mobileNumber + "\n\n" +

                            "🚀 Get Started:\n" +
                            "• Log in to explore all our features and make the most of your account!\n\n" +

                            "❓ Need Help?\n" +
                            "Visit our Help Center at https://www.servicesearch.com/help or reach us at support@servicesearch.com.\n\n" +

                            "Thanks for joining, and welcome aboard! 🎊\n\n" +

                            "Best regards,\n" +
                            "The ServiceSearch Team");

            javaMailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
    }

    // Method to send OTP for verification
    public boolean sendOtpToEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("🔒 Your OTP Code for Verification 🔒");

            message.setText("Hi there,\n\n" +
                            "Thank you for using ServiceSearch! 😊\n\n" +
                            "Your One-Time Password (OTP) for verification is:\n\n" +
                            "🔑 OTP Code: " + otp + "\n\n" +
                            "Please enter this code in the application to complete your verification.\n\n" +
                            "For your security, this OTP is valid for a short period. If you did not request this, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "The ServiceSearch Team\n\n" +
                            "📞 Contact us: support@servicesearch.com | 🌐 www.servicesearch.com");

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
            return false;
        }
    }

    // Method to send a password reset email
//    public boolean sendPasswordResetOtp(String email, String otp) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setSubject("Your OTP Code for Password Reset");
//            message.setText("Use the following OTP to reset your password: " + otp);
//            javaMailSender.send(message);
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error sending password reset OTP email: " + e.getMessage());
//            return false;
//        }
//    }
}
