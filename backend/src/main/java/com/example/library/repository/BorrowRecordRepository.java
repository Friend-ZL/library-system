package com.example.library.repository;

import com.example.library.entity.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    long countByUserIdAndStatusIn(Long userId, Iterable<String> statuses);

    long countByStatus(String status);

    long countByStatusIn(Iterable<String> statuses);

    Page<BorrowRecord> findByUserId(Long userId, Pageable pageable);

    Page<BorrowRecord> findByStatus(String status, Pageable pageable);

    long countByStatusAndDueDateBefore(String status, LocalDateTime dateTime);
}
