package com.example.bookstorecopy.entities;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class Cart {

    private List<BookDto> bookDtos = new ArrayList<>();

    public  int cartSize(){
        return bookDtos.size();
    }

    public void addToCart(BookDto bookDto){
        bookDtos.add(bookDto);
    }

    public List<BookDto> getBookDtos(){
        return bookDtos;
    }

    public void removeBookFromCart(BookDto bookDto){
        bookDtos.remove(bookDto);
    }

    public void clearCart(){
        bookDtos.clear();
    }
}
