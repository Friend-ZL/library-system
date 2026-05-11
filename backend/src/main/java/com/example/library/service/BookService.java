package com.example.library.service;

import com.example.library.common.PageResponse;
import com.example.library.entity.Book;
import com.example.library.exception.BusinessException;
import com.example.library.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public PageResponse<Book> page(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        if (keyword == null || keyword.trim().isEmpty()) {
            return PageResponse.from(bookRepository.findAll(pageable));
        }
        return PageResponse.from(bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(
                keyword, keyword, keyword, pageable));
    }

    public Book get(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BusinessException("图书不存在"));
    }

    @Transactional
    public Book save(Book book) {
        if (book.getId() == null && book.getIsbn() != null && !book.getIsbn().isEmpty() && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("ISBN已存在");
        }
        if (book.getAvailableCount() == null) {
            book.setAvailableCount(book.getTotalCount());
        }
        if (book.getTotalCount() != null && book.getAvailableCount() != null && book.getAvailableCount() > book.getTotalCount()) {
            throw new BusinessException("可借数量不能大于总库存");
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, Book input) {
        Book book = get(id);
        book.setIsbn(input.getIsbn());
        book.setTitle(input.getTitle());
        book.setAuthor(input.getAuthor());
        book.setPublisher(input.getPublisher());
        book.setPublishDate(input.getPublishDate());
        book.setCategoryId(input.getCategoryId());
        book.setCoverUrl(input.getCoverUrl());
        book.setDescription(input.getDescription());
        book.setTotalCount(input.getTotalCount());
        book.setAvailableCount(input.getAvailableCount());
        book.setStatus(input.getStatus());
        return save(book);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
