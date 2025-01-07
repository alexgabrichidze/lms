package com.library.service.exceptions;

/**
 * Exception thrown when a user-related operation fails due to invalid data.
 */
public class InvalidUserException extends RuntimeException {

    /**
     * Constructs a new InvalidUserException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidUserException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidUserException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}