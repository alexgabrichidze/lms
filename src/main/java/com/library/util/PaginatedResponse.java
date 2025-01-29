package com.library.util;

import java.util.List;

/**
 * A generic response wrapper for paginated API responses.
 * Includes data and pagination metadata.
 */
public class PaginatedResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public PaginatedResponse(List<T> data, int page, int size, long totalItems) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
    }

    // Getters
    public List<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
