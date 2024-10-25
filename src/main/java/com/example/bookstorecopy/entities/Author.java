package com.example.bookstorecopy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorName;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "author")
    private List<Book> bookList = new ArrayList<>();

    public void addBook(Book book){
        book.setAuthor(this);
        bookList.add(book);
    }
}
