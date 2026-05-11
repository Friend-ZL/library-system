package com.example.library.service;

import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StatisticsService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public StatisticsService(BookRepository bookRepository,
                             UserRepository userRepository,
                             BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public Map<String, Long> overview() {
        Map<String, Long> data = new LinkedHashMap<String, Long>();
        data.put("bookCount", bookRepository.count());
        data.put("userCount", userRepository.count());
        data.put("activeBorrowCount", borrowRecordRepository.countByStatusIn(Arrays.asList("BORROWED", "OVERDUE")));
        data.put("pendingBorrowCount", borrowRecordRepository.countByStatus("APPLYING"));
        data.put("overdueCount", borrowRecordRepository.countByStatusAndDueDateBefore("BORROWED", LocalDateTime.now())
                + borrowRecordRepository.countByStatus("OVERDUE"));
        return data;
    }
}
