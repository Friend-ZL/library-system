package com.example.library.config;

import com.example.library.entity.Book;
import com.example.library.entity.BookCategory;
import com.example.library.entity.User;
import com.example.library.repository.BookCategoryRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BookCategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           BookCategoryRepository categoryRepository,
                           BookRepository bookRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setRole("SUPER_ADMIN");
            admin.setStatus(1);
            userRepository.save(admin);
        }

        if (categoryRepository.count() == 0) {
            BookCategory literature = new BookCategory();
            literature.setName("文学");
            literature.setSort(1);
            categoryRepository.save(literature);

            BookCategory technology = new BookCategory();
            technology.setName("计算机");
            technology.setSort(2);
            categoryRepository.save(technology);
        }

        if (bookRepository.count() == 0) {
            Book book = new Book();
            book.setIsbn("9787115428028");
            book.setTitle("Spring Boot 实战");
            book.setAuthor("Craig Walls");
            book.setPublisher("人民邮电出版社");
            book.setPublishDate(LocalDate.of(2016, 9, 1));
            book.setTotalCount(5);
            book.setAvailableCount(5);
            book.setStatus(1);
            bookRepository.save(book);
        }
    }
}
