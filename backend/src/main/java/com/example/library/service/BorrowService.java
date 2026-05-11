package com.example.library.service;

import com.example.library.common.PageResponse;
import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.exception.BusinessException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class BorrowService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;

    @Value("${app.borrow.default-days}")
    private int defaultDays;

    @Value("${app.borrow.max-active-count}")
    private int maxActiveCount;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         BookRepository bookRepository,
                         CurrentUserService currentUserService) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public BorrowRecord apply(Long bookId) {
        User user = currentUserService.getCurrentUser();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException("图书不存在"));
        if (book.getStatus() == null || book.getStatus() != 1) {
            throw new BusinessException("图书未上架");
        }
        if (book.getAvailableCount() == null || book.getAvailableCount() <= 0) {
            throw new BusinessException("图书库存不足");
        }
        long activeCount = borrowRecordRepository.countByUserIdAndStatusIn(
                user.getId(), Arrays.asList("APPLYING", "BORROWED", "OVERDUE"));
        if (activeCount >= maxActiveCount) {
            throw new BusinessException("超过最大可借数量");
        }
        BorrowRecord record = new BorrowRecord();
        record.setUserId(user.getId());
        record.setBookId(bookId);
        record.setStatus("APPLYING");
        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord approve(Long id) {
        BorrowRecord record = get(id);
        if (!"APPLYING".equals(record.getStatus())) {
            throw new BusinessException("只有申请中的记录可以审批通过");
        }
        Book book = bookRepository.findById(record.getBookId()).orElseThrow(() -> new BusinessException("图书不存在"));
        if (book.getAvailableCount() == null || book.getAvailableCount() <= 0) {
            throw new BusinessException("图书库存不足");
        }
        book.setAvailableCount(book.getAvailableCount() - 1);
        record.setStatus("BORROWED");
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(defaultDays));
        bookRepository.save(book);
        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord reject(Long id) {
        BorrowRecord record = get(id);
        if (!"APPLYING".equals(record.getStatus())) {
            throw new BusinessException("只有申请中的记录可以拒绝");
        }
        record.setStatus("REJECTED");
        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord returnBook(Long id) {
        BorrowRecord record = get(id);
        if (!"BORROWED".equals(record.getStatus()) && !"OVERDUE".equals(record.getStatus())) {
            throw new BusinessException("只有已借出或逾期记录可以归还");
        }
        Book book = bookRepository.findById(record.getBookId()).orElseThrow(() -> new BusinessException("图书不存在"));
        book.setAvailableCount((book.getAvailableCount() == null ? 0 : book.getAvailableCount()) + 1);
        record.setStatus("RETURNED");
        record.setReturnDate(LocalDateTime.now());
        bookRepository.save(book);
        return borrowRecordRepository.save(record);
    }

    public PageResponse<BorrowRecord> page(String status, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        if (status == null || status.trim().isEmpty()) {
            return PageResponse.from(borrowRecordRepository.findAll(pageable));
        }
        return PageResponse.from(borrowRecordRepository.findByStatus(status, pageable));
    }

    public PageResponse<BorrowRecord> my(int page, int size) {
        User user = currentUserService.getCurrentUser();
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        return PageResponse.from(borrowRecordRepository.findByUserId(user.getId(), pageable));
    }

    public BorrowRecord get(Long id) {
        return borrowRecordRepository.findById(id).orElseThrow(() -> new BusinessException("借阅记录不存在"));
    }
}
