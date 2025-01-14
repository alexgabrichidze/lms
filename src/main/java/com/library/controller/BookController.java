package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.service.BookService;
import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for handling book-related HTTP requests.
 * Provides endpoints for managing books in the library, including CRUD
 * operations and search functionality.
 */
public class BookController extends BaseController {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    // BookService instance
    private final BookService bookService;

    /**
     * Constructs a new BookController with the custom BookService implementation.
     *
     * @param bookService The service used to perform book-related operations.
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Handles incoming HTTP requests and routes them to the appropriate method
     * based on the request path and method.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod(); // Get the HTTP method (e.g., GET, POST, PUT, DELETE)
        String path = exchange.getRequestURI().getPath(); // Get the request path (e.g., /books, /books/1)
        String query = exchange.getRequestURI().getQuery(); // Get the query parameters (e.g., title=1984)

        try {
            logger.info("Received {} request for path: {}", method, path);

            // Route the request based on the path
            if (path.matches("/books")) {
                if (query != null) {
                    handleSearchBooks(exchange, query); // Handle search functionality
                } else {
                    handleBooksEndpoint(exchange, method); // Handle /books endpoint
                }
            } else if (path.matches("/books/\\d+")) {
                int id = extractIdFromPath(path); // Extract the book ID from the path
                handleBookEndpoint(exchange, method, id); // Handle /books/{id} endpoint
            } else if (path.matches("/books/\\d+/status")) {
                int id = extractIdFromPath(path); // Extract the book ID from the path
                handleBookStatusEndpoint(exchange, method, id); // Handle /books/{id}/status endpoint
            } else {
                sendResponse(exchange, 404, "Not Found"); // Handle unknown paths
                logger.warn("Path not found: {}", path);
            }
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage(), e);
            sendResponse(exchange, 404, e.getMessage()); // Handle book not found errors
        } catch (InvalidBookException e) {
            logger.error("Invalid book data: {}", e.getMessage(), e);
            sendResponse(exchange, 400, e.getMessage()); // Handle invalid book data errors
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage(), e);
            sendResponse(exchange, 400, "Invalid input: " + e.getMessage()); // Handle invalid input errors
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage(), e);
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage()); // Handle unexpected errors
        }
    }

    /**
     * Handles requests to the /books endpoint.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param method   The HTTP method (e.g., GET, POST).
     * @throws IOException If an I/O error occurs while handling the
     *                     request.
     */
    private void handleBooksEndpoint(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                // Retrieve all books from the service
                List<Book> books = bookService.getAllBooks();
                sendResponse(exchange, 200, objectMapper.writeValueAsString(books)); // Send the list of books as JSON
                logger.info("Successfully retrieved all books");
                break;
            case "POST":
                // Parse the request body into a Book object
                Book book = parseRequestBody(exchange, Book.class);
                bookService.addBook(book); // Add the new book to the service
                sendResponse(exchange, 201, "Book added successfully"); // Send a success response
                logger.info("Successfully added book with ID: {}", book.getId());
                break;
            default:
                // Handle unsupported HTTP methods
                sendResponse(exchange, 405, "Method Not Allowed");
                logger.warn("Method not allowed for /books endpoint: {}", method);
        }
    }

    /**
     * Handles requests to the /books/{id} endpoint.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param method   The HTTP method (e.g., GET, PUT, DELETE).
     * @param id       The ID of the book to operate on.
     * @throws IOException If an I/O error occurs while handling the
     *                     request.
     */
    private void handleBookEndpoint(HttpExchange exchange, String method, int id)
            throws IOException {
        switch (method) {
            case "GET":
                // Retrieve the book by ID from the service
                Book book = bookService.getBookById(id);
                sendResponse(exchange, 200, objectMapper.writeValueAsString(book)); // Send the book details as JSON
                logger.info("Successfully retrieved book with ID: {}", id);
                break;
            case "PUT":
                // Parse the request body into a Book object
                Book updatedBook = parseRequestBody(exchange, Book.class);
                updatedBook.setId(id); // Ensure the ID matches the path
                bookService.updateBook(updatedBook); // Update the book in the service
                sendResponse(exchange, 200, "Book updated successfully"); // Send a success response
                logger.info("Successfully updated book with ID: {}", id);
                break;
            case "DELETE":
                // Delete the book by ID from the service
                bookService.deleteBook(id);
                sendResponse(exchange, 204, ""); // Send a success response with no content
                logger.info("Successfully deleted book with ID: {}", id);
                break;
            default:
                // Handle unsupported HTTP methods
                sendResponse(exchange, 405, "Method Not Allowed");
                logger.warn("Method not allowed for book endpoint: {}", method);
        }
    }

    /**
     * Handles search requests to the /books endpoint with query parameters.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param query    The query string (e.g., title=1984, author=George+Orwell).
     * @throws IOException If an I/O error occurs while handling the
     *                     request.
     */
    private void handleSearchBooks(HttpExchange exchange, String query) throws IOException {
        // Parse the query parameters into a map
        Map<String, String> queryParams = parseQueryParameters(query);

        if (queryParams.containsKey("title")) {

            // Search books by title
            String title = queryParams.get("title");
            List<Book> books = bookService.getBooksByTitle(title);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books)); // Send the list of books as JSON
            logger.info("Successfully searched books by title: {}", title);
        } else if (queryParams.containsKey("author")) {

            // Search books by author
            String author = queryParams.get("author");
            List<Book> books = bookService.getBooksByAuthor(author);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books)); // Send the list of books as JSON
            logger.info("Successfully searched books by author: {}", author);
        } else if (queryParams.containsKey("isbn")) {

            // Search books by ISBN
            String isbn = queryParams.get("isbn");
            Book book = bookService.getBookByIsbn(isbn);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(book)); // Send the book details as JSON
            logger.info("Successfully searched book by ISBN: {}", isbn);
        } else {

            // Handle invalid search parameters
            sendResponse(exchange, 400, "Invalid search parameter");
            logger.warn("Invalid search parameter: {}", query);
        }
    }

    /**
     * Handles requests to the /books/{id}/status endpoint.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param method   The HTTP method (e.g., PUT).
     * @param id       The ID of the book to update.
     * @throws IOException If an I/O error occurs while handling the
     *                     request.
     */
    private void handleBookStatusEndpoint(HttpExchange exchange, String method, int id)
            throws IOException {

        if (method.equals("PUT")) {

            // Parse the request body to get the new status
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            logger.debug("Request body received: {}", requestBody); // Log the request
            // body

            String status = objectMapper.readValue(requestBody, String.class); // Parse status from request body

            // Validate the status before updating
            try {
                BookStatus bookStatus = BookStatus.valueOf(status); // Convert the status string to an enum
                bookService.updateBookStatus(id, bookStatus); // Update the book status in the service
                sendResponse(exchange, 200, "Book status updated successfully"); // Send a success response
                logger.info("Successfully updated status for book with ID: {}", id);
            } catch (IllegalArgumentException e) {

                // Handle invalid status values
                sendResponse(exchange, 400, "Invalid book status: " + status);
                logger.warn("Invalid book status: {}", status);
            }
        } else {

            // Handle unsupported HTTP methods
            sendResponse(exchange, 405, "Method Not Allowed");
            logger.warn("Method not allowed for book status endpoint: {}", method);
        }
    }

    /**
     * Parses query parameters from the query string.
     *
     * @param query The query string (e.g., "title=1984&author=George+Orwell").
     * @return A map of query parameters.
     */
    private Map<String, String> parseQueryParameters(String query) {
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