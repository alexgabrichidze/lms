package com.library.util;

import java.util.function.Supplier;

/**
 * Utility class for validating input data.
 */
public class ValidationUtil {

    /**
     * Validates that a string is not null or empty and throws a custom exception if
     * invalid.
     *
     * @param value             the string to validate
     * @param fieldName         the name of the field for error messages
     * @param exceptionSupplier a supplier for the custom exception to throw
     * @param <T>               the type of the exception to throw
     * @throws T if the validation fails
     */
    public static <T extends RuntimeException> void validateNotEmpty(
            String value, String fieldName, Supplier<T> exceptionSupplier) {
        if (value == null || value.trim().isEmpty()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates that an ID is a positive integer and throws a custom exception if
     * invalid.
     *
     * @param id                the ID to validate
     * @param fieldName         the name of the field for error messages
     * @param exceptionSupplier a supplier for the custom exception to throw
     * @param <T>               the type of the exception to throw
     * @throws T if the validation fails
     */
    public static <T extends RuntimeException> void validatePositiveId(
            int id, String fieldName, Supplier<T> exceptionSupplier) {
        if (id <= 0) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates the format of an email address and throws a custom exception if
     * invalid.
     *
     * @param email             the email to validate
     * @param exceptionSupplier a supplier for the custom exception to throw
     * @param <T>               the type of the exception to throw
     * @throws T if the validation fails
     */
    public static <T extends RuntimeException> void validateEmail(
            String email, Supplier<T> exceptionSupplier) {
        validateNotEmpty(email, "Email", exceptionSupplier);
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw exceptionSupplier.get();
        }
    }
}
