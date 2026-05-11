package com.example.library.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowApplyRequest {
    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
