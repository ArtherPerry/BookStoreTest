package com.example.bookstorecopy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Make sure this matches your Thymeleaf template file name (e.g., login.html)
    }
}
