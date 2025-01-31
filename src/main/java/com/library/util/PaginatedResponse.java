package com.library.util;

import java.util.List;

/**
 * A generic response wrapper for paginated API responses.
 * Includes data and pagination metadata.
 * Supports both page-based and cursor-based pagination.
 */
public class PaginatedResponse<T> {
    private List<T> data; // The list of items in the current page
    private int page; // Current page number (for page-based pagination)
    private int size; // Number of items per page
    private long totalItems; // Total number of items across all pages
    private Integer totalPages; // Total number of pages (for page-based pagination)
    private String nextCursor; // Cursor for the next page (for cursor-based pagination)

    /**
     * Constructor for page-based pagination.
     *
     * @param data       The list of items in the current page.
     * @param page       The current page number.
     * @param size       The number of items per page.
     * @param totalItems The total number of items across all pages.
     */
    public PaginatedResponse(List<T> data, int page, int size, long totalItems) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
        this.nextCursor = null;
    }

    /**
     * Constructor for cursor-based pagination.
     *
     * @param data       The list of items in the current page.
     * @param nextCursor The cursor for the next page.
     * @param size       The number of items per page.
     * @param totalItems The total number of items across all pages.
     */
    public PaginatedResponse(List<T> data, String nextCursor, int size, long totalItems) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = null;
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

    public Integer getTotalPages() {
        return totalPages;
    }

    public String getNextCursor() {
        return nextCursor;
    }
}