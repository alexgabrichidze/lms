package com.library.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.library.service.exceptions.InvalidBookException;

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

    /**
     * Validates that multiple string fields are not null or empty.
     *
     * @param fieldAndNamePairs Pairs of fields and their names (e.g., "title",
     *                          "Title").
     * @throws InvalidBookException If any field is null or empty.
     */
    public static void validateFieldsNotEmpty(String... fieldAndNamePairs) {
        for (int i = 0; i < fieldAndNamePairs.length; i += 2) {
            String field = fieldAndNamePairs[i];
            String fieldName = fieldAndNamePairs[i + 1];

            if (field == null || field.trim().isEmpty()) {
                throw new InvalidBookException(fieldName + " cannot be null or empty.");
            }
        }
    }

    /**
     * Parses query parameters from the query string.
     *
     * @param query The query string (e.g., "title=1984&author=George+Orwell").
     * @return A map of query parameters.
     */
    public static Map<String, String> parseQueryParameters(String query) {
        return Stream.of(query.split("&")) // Split the query string by "&"
                .map(param -> param.split("=")) // Split each parameter by "="
                .collect(Collectors.toMap(
                        arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8), // Decode the parameter key
                        arr -> URLDecoder.decode(arr.length > 1 ? arr[1] : "", StandardCharsets.UTF_8) // Decode the
                                                                                                       // parameter
                                                                                                       // value
                ));
    }
}
