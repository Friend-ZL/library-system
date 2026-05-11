package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.entity.BookCategory;
import com.example.library.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<BookCategory>> list() {
        return ApiResponse.ok(categoryService.list());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BookCategory> create(@RequestBody BookCategory category) {
        return ApiResponse.ok(categoryService.save(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BookCategory> update(@PathVariable Long id, @RequestBody BookCategory category) {
        category.setId(id);
        return ApiResponse.ok(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.ok(null);
    }
}
