package com.example.bookstorecopy.service;

import com.example.bookstorecopy.entities.Category;
import com.example.bookstorecopy.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public void saveCategory(Category category){
        categoryRepo.save(category);
    }

    public List<Category> findAllCategories(){
        return categoryRepo.findAll();
    }

}
