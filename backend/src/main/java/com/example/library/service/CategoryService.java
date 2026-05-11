package com.example.library.service;

import com.example.library.entity.BookCategory;
import com.example.library.repository.BookCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final BookCategoryRepository categoryRepository;

    public CategoryService(BookCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<BookCategory> list() {
        return categoryRepository.findAllByOrderBySortAscIdAsc();
    }

    public BookCategory save(BookCategory category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
