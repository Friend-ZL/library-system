package com.example.library.repository;

import com.example.library.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    List<BookCategory> findAllByOrderBySortAscIdAsc();
}
