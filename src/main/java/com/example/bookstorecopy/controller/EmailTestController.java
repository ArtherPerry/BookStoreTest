package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-test-email")
    public String sendTestEmail(@RequestParam String toEmail) {
        String subject = "Test Email";
        String body = "This is a test email from the Bookstore application.";
        emailService.sendConfirmationEmail(toEmail, subject, body);
        return "Test email sent to " + toEmail;
    }
}
