package com.secureauthsystem.service;

import com.secureauthsystem.dto.OtpVerificationEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender; // Injected to send emails

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Method to send a concise, user-friendly welcome email with icons and symbols
    public void sendWelcomeEmail(String email, String username, String password, String mobileNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("🎉 Welcome to MockSkills! Your Account is Ready 🎉");

            message.setText("Hi " + username + ",\n\n" +
                            "👋 Welcome to MockSkills! We’re thrilled to have you with us. 😊\n\n" +

                            "🔑 Your Account Details:\n" +
                            "• 👤 Username: " + username + "\n" +
                            "• 📧 Email: " + email + "\n" +
                            "• 🔒 Password: " + password + " (please change it after your first login)\n" +
                            "• 📱 Mobile: " + mobileNumber + "\n\n" +

                            "🚀 Get Started:\n" +
                            "• Explore all our features by logging in today!\n\n" +

                            "❓ Need Help?\n" +
                            "Visit our Help Center at https://www.mockskills.com/help or reach us at support@mockskills.com.\n\n" +

                            "Thanks for joining, and welcome aboard! 🎊\n\n" +

                            "Best,\n" +
                            "Kundan Singh\n" +
                            "Founder, MockSkills\n\n" +

                            "📞 Contact us: info@mockskills.com | 🌐 www.mockskills.com");

            javaMailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
    }

    // Method to send OTP to the email
    public boolean sendOtpToEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("🔒 Your OTP Code for Verification 🔒");

            message.setText("Hi there,\n\n" +
                            "Thank you for using MockSkills! 😊\n\n" +
                            "Your One-Time Password (OTP) for verification is:\n\n" +
                            "🔑 OTP Code: " + otp + "\n\n" +
                            "Please enter this code in the application to complete your verification.\n\n" +
                            "For your security, this OTP is valid for a short period of time. If you did not request this, please ignore this email.\n\n" +
                            "If you need any assistance, feel free to reach out to our support team.\n\n" +
                            "Best regards,\n" +
                            "The MockSkills Team\n\n" +
                            "📞 Contact us: info@mockskills.com | 🌐 www.mockskills.com");

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
}
