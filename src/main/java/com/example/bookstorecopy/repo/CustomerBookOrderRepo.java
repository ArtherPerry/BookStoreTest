package com.example.bookstorecopy.repo;

import com.example.bookstorecopy.entities.CustomerBookOrder;
import com.example.bookstorecopy.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerBookOrderRepo extends JpaRepository<CustomerBookOrder,Long> {

    List<CustomerBookOrder> findCustomerBookOrdersByOrderStatus(OrderStatus orderStatus);
}
