package com.library.service.exceptions;

/**
 * Exception thrown when a loan is not found.
 */
public class LoanNotFoundException extends RuntimeException {

    /**
     * Constructs a new LoanNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public LoanNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new LoanNotFoundException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public LoanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
