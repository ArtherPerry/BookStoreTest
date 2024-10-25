package com.example.bookstorecopy.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private String address;
    private int zipcode;
    private String email;

    // Add payment method field
    private String paymentMethod;

    @Column(nullable = true)
    private String paymentReceiptPath;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomerBookOrder> customerBookOrders = new ArrayList<>();

    // Removed the purchasedBooks list as it is redundant with the CustomerBookOrder entity.
}
