package com.example.bookstorecopy.repo;

import com.example.bookstorecopy.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Long> {
}
