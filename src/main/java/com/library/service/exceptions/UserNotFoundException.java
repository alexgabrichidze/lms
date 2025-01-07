package com.library.service.exceptions;

/**
 * Exception thrown when a user-related operation fails due to the user not
 * being found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}