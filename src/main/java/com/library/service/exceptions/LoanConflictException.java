package com.library.service.exceptions;

/**
 * Exception thrown when a loan conflicts with existing data or rules.
 */
public class LoanConflictException extends RuntimeException {

    /**
     * Constructs a new LoanConflictException with the specified detail message.
     *
     * @param message the detail message
     */
    public LoanConflictException(String message) {
        super(message);
    }

    /**
     * Constructs a new LoanConflictException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public LoanConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
