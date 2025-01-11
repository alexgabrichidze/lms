package com.library.service.exceptions;

/**
 * Exception thrown when loan data is invalid.
 */
public class InvalidLoanException extends RuntimeException {

    /**
     * Constructs a new InvalidLoanException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidLoanException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidLoanException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public InvalidLoanException(String message, Throwable cause) {
        super(message, cause);
    }
}
