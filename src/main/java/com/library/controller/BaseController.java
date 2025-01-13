package com.library.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * BaseController provides common functionality for all controllers.
 * It handles sending HTTP responses, parsing request bodies, and extracting IDs
 * from URLs.
 */
public abstract class BaseController implements HttpHandler {

    // Jackson ObjectMapper for JSON serialization/deserialization
    protected final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sends an HTTP response with the given status code and response body.
     *
     * @param exchange   The HttpExchange object representing the HTTP
     *                   request/response.
     * @param statusCode The HTTP status code to send (e.g., 200, 404, 500).
     * @param response   The response body as a string.
     * @throws IOException If an I/O error occurs while sending the response.
     */

    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        // If it's 204 (No Content), don't send a response body at all
        if (statusCode == 204) {
            // Indicate no body by passing -1
            exchange.sendResponseHeaders(statusCode, -1);
            // It’s good practice to close the exchange here
            exchange.close();
            return;
        }

        // Otherwise, handle a normal body response
        String responseWithNewline = response + "\n";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseWithNewline.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseWithNewline.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Extracts the ID from the URL path (e.g., /books/1 -> 1).
     *
     * @param path The URL path (e.g., "/books/1").
     * @return The extracted ID as an integer.
     * @throws NumberFormatException If the ID in the path is not a valid integer.
     */
    protected int extractIdFromPath(String path) {
        String[] parts = path.split("/");

        // If the path ends with "status", then the ID is in parts[parts.length - 2].
        if (parts[parts.length - 1].equals("status")) {
            return Integer.parseInt(parts[parts.length - 2]);
        } else {
            return Integer.parseInt(parts[parts.length - 1]);
        }
    }

    /**
     * Parses the request body into a Java object of the specified class.
     *
     * @param exchange The HttpExchange object representing the HTTP
     *                 request/response.
     * @param clazz    The class of the object to parse the JSON into.
     * @param <T>      The type of the object to parse.
     * @return The parsed Java object.
     * @throws IOException If an I/O error occurs while reading the request body.
     */
    protected <T> T parseRequestBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        // Read the request body into a string
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        // Parse the JSON string into a Java object using Jackson
        return objectMapper.readValue(requestBody, clazz);
    }

    /**
     * Handles the HTTP request. This method must be implemented by subclasses.
     *
     * @param exchange The HttpExchange object representing the HTTP
     *                 request/response.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    @Override
    public abstract void handle(HttpExchange exchange) throws IOException;
}
