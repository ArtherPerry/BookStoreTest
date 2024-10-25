package com.example.bookstorecopy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jhon.engineer@gmail.com"); // Sender email
        message.setTo(to); // Receiver email (customer's email)
        message.setSubject(subject); // Email subject
        message.setText(body); // Email body content
        mailSender.send(message); // Send email
    }
}
