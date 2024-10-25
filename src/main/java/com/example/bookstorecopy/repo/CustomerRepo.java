package com.example.bookstorecopy.repo;

import com.example.bookstorecopy.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer,Long> {

    Optional<Customer> findCustomerByName(String name);
}
