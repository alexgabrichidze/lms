package com.library.service.exceptions;

/**
 * Exception thrown when a book-related operation fails due to the book not
 * being found.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new BookNotFoundException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}