package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.entities.Author;
import com.example.bookstorecopy.entities.Book;
import com.example.bookstorecopy.entities.Category;
import com.example.bookstorecopy.entities.OrderStatus;
import com.example.bookstorecopy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;



    @GetMapping("/dashboard")
    public String adminHomePage(Model model){
        model.addAttribute("category",new Category());
        model.addAttribute("author",new Author());
        model.addAttribute("book",new Book());
        model.addAttribute("authors",authorService.findAllAuthor());
        model.addAttribute("categories",categoryService.findAllCategories());
        model.addAttribute("books",bookService.findAllBook());
        model.addAttribute("placedOrders",customerService.findPlacedOrders());
        model.addAttribute("allOrders",customerService.findAllOrders());
        return "dashboard";
    }

    @PostMapping("/order/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus newStatus, RedirectAttributes redirectAttributes) {
        try {
            customerService.updateOrderStatus(id, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update order status!");
        }
        return "redirect:/admin/dashboard"; // Redirect to the all orders page
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete order!");
        }
        return "redirect:/admin/dashboard"; // Redirect to the all orders page
    }

    @GetMapping("/order/findAll")
    public String findAllOrders(Model model){
        model.addAttribute("allOrders",customerService.findAllOrders());
        return "allOrdersTable";
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestParam String username,
                                             @RequestParam String password,
                                             @RequestParam String[] roles) {
        userService.createUser(username, password, roles);
        return ResponseEntity.ok("User created successfully.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String username,
                                                 @RequestParam String newPassword) {
        userService.changePassword(username, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String username) {
        String defaultPassword = "defaultPassword123"; // Or generate a random one
        userService.resetPassword(username, defaultPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }
}




