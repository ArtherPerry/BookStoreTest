package com.example.bookstorecopy.service;

import com.example.bookstorecopy.entities.*;
import com.example.bookstorecopy.repo.BookRepo;
import com.example.bookstorecopy.repo.CustomerBookOrderRepo;
import com.example.bookstorecopy.repo.CustomerRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerBookOrderRepo customerBookOrderRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BookRepo bookRepo;

    public void saveCustomerBookOrder(Customer customer, List<BookDto> bookDtoList) {
        CustomerBookOrder customerBookOrder = new CustomerBookOrder();
        customerBookOrder.setCustomer(customer);
        customerBookOrder.setTotalAmount(totalPricesInCents(bookDtoList));
        customerBookOrder.setOrderCode(generateCode(customer));
        customerBookOrder.setOrderStatus(OrderStatus.PLACED);

        // Convert BookDto to Book, retrieve existing books, or create new ones as needed
        List<Book> books = bookDtoList.stream()
                .map(bookDto -> {
                    Book book = convertToBook(bookDto); // Assuming this converts the DTO to a Book
                    if (book.getId() != null) {
                        // If the book ID is set, try to retrieve the existing entity from the database
                        return bookRepo.findById(book.getId())
                                .orElse(book); // If not found, return the newly created book
                    }
                    return book; // New book will be persisted
                })
                .collect(Collectors.toList());
        customerBookOrder.setBooks(books);

        // Save the customerBookOrder
        customerBookOrderRepo.save(customerBookOrder);
    }


    private Book convertToBook(BookDto bookDto) {
        // Implement the conversion logic from BookDto to Book entity
        Book book = new Book();
        book.setId(bookDto.getId()); // Assuming BookDto has an 'id' field
        book.setTitle(bookDto.getTitle()); // Assuming BookDto has a 'title' field
        book.setPrice(bookDto.getPrice()); // Assuming BookDto has a 'price' field
        // Set other fields as necessary
        return book;
    }


    private String generateCode(Customer customer){
        Random random = new Random();
        int code = random.nextInt(100000)+100000;
        return customer.getName()+code;
    }


    public int totalPricesInCents(List<BookDto> bookDtos) {
        // Calculate total in dollars (or the base currency)
        double totalInDollars = bookDtos.stream().map(BookDto::getPrice).mapToDouble(d -> d).sum();

        // Convert total amount to cents and return as an integer
        return (int) (totalInDollars * 100);
    }

    public List<CustomerBookOrder> findAllOrders(){
       return customerBookOrderRepo.findAll();
    }

    public List<CustomerBookOrder> findPlacedOrders(){
        return customerBookOrderRepo.findCustomerBookOrdersByOrderStatus(OrderStatus.PLACED);
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        CustomerBookOrder order = customerBookOrderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        order.setOrderStatus(newStatus);
        customerBookOrderRepo.save(order); // Save the updated order
    }

    public void deleteOrder(Long orderId) {
        customerBookOrderRepo.deleteById(orderId); // Delete the order by ID
    }


}
