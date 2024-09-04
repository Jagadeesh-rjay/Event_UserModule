package com.example.demo.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;

@Component
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    public ResponseEntity<Category> getCategoryById(Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Category> createCategory(Category category) {
        Category createdCategory = categoryService.save(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    public ResponseEntity<Category> updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryService.findById(id);
        if (existingCategory.isPresent()) {
            category.setId(id);
            Category updatedCategory = categoryService.save(category);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Void> deleteCategory(Long id) {
        Optional<Category> existingCategory = categoryService.findById(id);
        if (existingCategory.isPresent()) {
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
