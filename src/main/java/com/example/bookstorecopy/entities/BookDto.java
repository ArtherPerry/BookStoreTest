package com.example.bookstorecopy.entities;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookDto {

    private Long id;
    private String title;
    private double price;
    private LocalDate yearPublished;
    private String description;
    private String imageUrl;
    private Long categoryId;
    private Long authorId;

    public BookDto() {
    }

    public BookDto(Long id, String title, double price, LocalDate yearPublished, String description, String imageUrl, Long categoryId, Long authorId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.yearPublished = yearPublished;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.authorId = authorId;
    }
}
