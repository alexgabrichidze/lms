package com.library.model;

import java.time.LocalDate;

/**
 * Represents a book in the library management system.
 * Includes attributes such as id, title, author, ISBN, published date, and
 * status.
 */
public class Book {
    private int id; // Unique identifier for the book
    private String title; // Title of the book
    private String author; // Author of the book
    private String isbn; // ISBN of the book (13-digit numeric string)
    private LocalDate publishedDate; // Date the book was published
    private BookStatus status; // Status of the book (e.g., AVAILABLE, BORROWED)

    /**
     * Default constructor for creating an empty Book object.
     */
    public Book() {
    }

    /**
     * Parameterized constructor for creating a Book with specified attributes.
     *
     * @param title         the title of the book
     * @param author        the author of the book
     * @param isbn          the ISBN of the book
     * @param publishedDate the publication date of the book
     * @param status        the status of the book
     */
    public Book(String title, String author, String isbn, LocalDate publishedDate, BookStatus status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.status = status;
    }

    /**
     * Parameterized constructor for creating a Book with specified attributes.
     *
     * @param id            the id of the book
     * @param title         the title of the book
     * @param author        the author of the book
     * @param isbn          the ISBN of the book
     * @param publishedDate the publication date of the book
     * @param status        the status of the book
     */
    public Book(int id, String title, String author, String isbn, LocalDate publishedDate, BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.status = status;
    }

    /**
     * Gets the unique ID of the book.
     *
     * @return the book's ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique ID of the book.
     *
     * @param id the book's ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title of the book.
     *
     * @return the book's title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the book's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the book's author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author the book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return the book's ISBN
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the book's ISBN
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the publication date of the book.
     *
     * @return the book's publication date
     */
    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }

    /**
     * Sets the publication date of the book.
     *
     * @param publishedDate the book's publication date
     */
    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * Gets the status of the book.
     *
     * @return the book's status
     */
    public BookStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the book.
     *
     * @param status the book's status
     */
    public void setStatus(BookStatus status) {
        this.status = status;
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
                ", status=" + status +
                '}';
    }
}
