package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.common.PageResponse;
import com.example.library.dto.BorrowApplyRequest;
import com.example.library.entity.BorrowRecord;
import com.example.library.service.BorrowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/apply")
    public ApiResponse<BorrowRecord> apply(@Valid @RequestBody BorrowApplyRequest request) {
        return ApiResponse.ok(borrowService.apply(request.getBookId()));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BorrowRecord> approve(@PathVariable Long id) {
        return ApiResponse.ok(borrowService.approve(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BorrowRecord> reject(@PathVariable Long id) {
        return ApiResponse.ok(borrowService.reject(id));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BorrowRecord> returnBook(@PathVariable Long id) {
        return ApiResponse.ok(borrowService.returnBook(id));
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<BorrowRecord>> my(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(borrowService.my(page, size));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PageResponse<BorrowRecord>> page(@RequestParam(required = false) String status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(borrowService.page(status, page, size));
    }
}
