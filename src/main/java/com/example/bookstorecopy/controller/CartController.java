package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.entities.Book;
import com.example.bookstorecopy.entities.BookDto;
import com.example.bookstorecopy.service.BookService;
import com.example.bookstorecopy.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id){
        cartService.addToCart(bookService.findBookById(id));
        return "redirect:/shop/show-all-books";
    }

    @GetMapping("/shop/cart/view")
    public String viewCart(Model model){
        model.addAttribute("cartItems",cartService.listCart());
        model.addAttribute("cartSize",cartService.cartSize());
        model.addAttribute("bookDto",new BookDto());
        return "cart-view";
    }

    @GetMapping("/cart/delete/{id}")
    public String removeFromCart(@PathVariable("id")Long id,Model model){
        model.addAttribute("cartSize",cartService.cartSize());
        Book book = bookService.findBookById(id);
        cartService.remove(cartService.toDto(book));
        return "redirect:/shop/cart/view";
    }

    @GetMapping("/cart/clear")
    public String clearCart(){
        cartService.clearCart();
        return "redirect:/shop/cart/view";
    }
}
