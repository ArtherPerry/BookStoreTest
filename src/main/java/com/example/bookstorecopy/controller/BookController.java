package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.entities.Book;
import com.example.bookstorecopy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/save")
    public String saveBook(Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "home";
        }
        bookService.saveBook(book,book.getCategoryId(),book.getAuthorId());
        return "redirect:/";
    }

    @GetMapping("/findAll")
    public String listAllBooks(Model model){
        model.addAttribute("books",bookService.findAllBook());
        return "list-book";
    }

    @GetMapping("/find/details")
    public String bookDetails(@RequestParam("id") Long id, Model model){
        model.addAttribute("book",bookService.findBookById(id));
        return "book-details";
    }

}
