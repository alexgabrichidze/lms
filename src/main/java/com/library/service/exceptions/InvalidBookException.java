package com.library.service.exceptions;

/**
 * Exception thrown when a book-related operation fails due to invalid data.
 */
public class InvalidBookException extends RuntimeException {

    /**
     * Constructs a new InvalidBookException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidBookException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidBookException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public InvalidBookException(String message, Throwable cause) {
        super(message, cause);
    }
}