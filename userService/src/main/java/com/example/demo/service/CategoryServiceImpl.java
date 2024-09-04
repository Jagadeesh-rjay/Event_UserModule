package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryDAO;

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryDAO.findById(id);
    }

    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    public void deleteById(Long id) {
        categoryDAO.deleteById(id);
    }
    
	@Override
	public Category addCategory(Category category) {
		return this.categoryDAO.save(category);
	}

	@Override
	public Category updateCategory(Category category) {
		return this.categoryDAO.save(category);
	}

   
    
    @Override
	public List<Category> getCategoriesByStatusIn(List<String> status) {
		return this.categoryDAO.findByStatusIn(status);
	}

	@Override
	public Category getCategoryById(long categoryId) {

		Optional<Category> optionalCategory = this.categoryDAO.findById(categoryId);

		if (optionalCategory.isPresent()) {
			return optionalCategory.get();
		} else {
			return null;
		}

	}
    
    
}
