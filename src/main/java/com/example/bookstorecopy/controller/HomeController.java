package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.entities.Book;

import com.example.bookstorecopy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/","/home"})
    public String index(){

        return "home";
    }

    @ModelAttribute("allbooks")
    public List<Book> showAllBook(){
        return bookService.findAllBook();
    }

    @GetMapping("/shop/show-all-books")
    public String listBooks(){
        return "list-books";
    }

}
