package com.example.bookstorecopy.repo;

import com.example.bookstorecopy.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author,Long> {
}
