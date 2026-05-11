package com.example.library.common;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {
    private List<T> records;
    private long total;
    private int page;
    private int size;

    public PageResponse() {
    }

    public PageResponse(List<T> records, long total, int page, int size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<T>(page.getContent(), page.getTotalElements(), page.getNumber() + 1, page.getSize());
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
