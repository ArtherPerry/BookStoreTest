package com.example.bookstorecopy.repo;

import com.example.bookstorecopy.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
