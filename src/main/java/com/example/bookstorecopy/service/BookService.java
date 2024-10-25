package com.example.bookstorecopy.service;

import com.example.bookstorecopy.entities.Author;
import com.example.bookstorecopy.entities.Book;
import com.example.bookstorecopy.entities.Category;
import com.example.bookstorecopy.repo.AuthorRepo;
import com.example.bookstorecopy.repo.BookRepo;
import com.example.bookstorecopy.repo.CategoryRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Transactional
    public void saveBook(Book book,Long categoryId,Long authorId){
        Category category = categoryRepo.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Author author = authorRepo.findById(authorId).orElseThrow(EntityNotFoundException::new);
        category.addBook(book);
        author.addBook(book);
        bookRepo.save(book);
    }

    public List<Book> findAllBook(){
        return bookRepo.findAll();
    }

    public Book findBookById(Long id){
     return bookRepo.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
