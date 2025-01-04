package com.library.model;

import java.time.LocalDate;

/**
 * Represents a book in the library management system.
 * Includes attributes such as id, title, author, ISBN, published date, and status.
 */
public class Book {
    private int id; // Unique identifier for the book (auto-generated by the database)
    private String title; // Title of the book
    private String author; // Author of the book
    private String isbn; // ISBN of the book (13-digit numeric string)
    private LocalDate publishedDate; // Date the book was published
    private String status; // Status of the book (e.g., AVAILABLE, BORROWED)

    /**
     * Default constructor for creating an empty Book object.
     */
    public Book() {
    }

    /**
     * Parameterized constructor for creating a Book with specified attributes.
     *
     * @param title         the title of the book (must not be null or empty)
     * @param author        the author of the book (must not be null or empty)
     * @param isbn          the ISBN of the book (must be a 13-digit numeric string)
     * @param publishedDate the publication date of the book (must not be in the
     *                      future)
     * @param status        the status of the book (must be either AVAILABLE or
     *                      BORROWED)
     */
    public Book(String title, String author, String isbn, LocalDate publishedDate, String status) {
        setTitle(title); // Validates and sets the title
        setAuthor(author); // Validates and sets the author
        setIsbn(isbn); // Validates and sets the ISBN
        setPublishedDate(publishedDate); // Validates and sets the publication date
        setStatus(status); // Validates and sets the status
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return the book ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param id the book ID (must be non-negative)
     * @throws IllegalArgumentException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
        this.id = id;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title of the book (must not be null or empty)
     * @throws IllegalArgumentException if the title is null or empty
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author the author of the book (must not be null or empty)
     * @throws IllegalArgumentException if the author is null or empty
     */
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        this.author = author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return the ISBN of the book
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the ISBN of the book (must be a 13-digit numeric string)
     * @throws IllegalArgumentException if the ISBN is null, empty, or invalid
     */
    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty() || isbn.length() != 13 || !isbn.matches("\\d+")) {
            throw new IllegalArgumentException("ISBN must be a 13-digit numeric string.");
        }
        this.isbn = isbn;
    }

    /**
     * Gets the publication date of the book.
     *
     * @return the publication date of the book
     */
    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }

    /**
     * Sets the publication date of the book.
     *
     * @param publishedDate the publication date of the book (must not be in the
     *                      future)
     * @throws IllegalArgumentException if the publication date is in the future
     */
    public void setPublishedDate(LocalDate publishedDate) {
        if (publishedDate != null && publishedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Published date cannot be in the future.");
        }
        this.publishedDate = publishedDate;
    }

    /**
     * Gets the status of the book.
     *
     * @return the status of the book
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the book.
     *
     * @param status the status of the book (must be either AVAILABLE or BORROWED)
     * @throws IllegalArgumentException if the status is null or invalid
     */
    public void setStatus(String status) {
        if (status == null || (!status.equalsIgnoreCase("AVAILABLE") && !status.equalsIgnoreCase("BORROWED"))) {
            throw new IllegalArgumentException("Status must be either 'AVAILABLE' or 'BORROWED'.");
        }
        this.status = status.toUpperCase(); // Ensures the status is stored in uppercase
    }

    /**
     * Provides a string representation of the book.
     *
     * @return a string representation of the book
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishedDate=" + publishedDate +
                ", status='" + status + '\'' +
                '}';
    }
}