package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.service.BookService;
import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.library.util.ValidationUtil.*;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
            // Log the incoming request
            logger.info("Received {} request for path: {}", method, path);

            // Route the request based on the path
            if (path.matches("/books")) {
                if (query != null) {
                    handleSearchBooks(exchange, query, method); // Handle search functionality
                } else {
                    handleBooksEndpoint(exchange, method); // Handle /books endpoint
                }
            } else if (path.matches("/books/\\d+")) {
                int id = extractIdFromPath(path);

                // Validate the ID is a positive integer
                validatePositiveId(id, "Book ID",
                        () -> new InvalidBookException("Book ID must be a positive integer."));
                handleBookEndpoint(exchange, method, id); // Handle /books/{id} endpoint
            } else if (path.matches("/books/\\d+/status")) {
                int id = extractIdFromPath(path);

                // Validate the ID is a positive integer
                validatePositiveId(id, "Book ID",
                        () -> new InvalidBookException("Book ID must be a positive integer."));
                handleBookStatusEndpoint(exchange, method, id); // Handle /books/{id}/status endpoint
            } else {

                // Handle unknown paths
                sendResponse(exchange, 404, "Not Found"); // Handle unknown paths
                logger.warn("Path not found: {}", path);
            }
        } catch (BookNotFoundException e) {

            // Handle book not found errors
            logger.error("Book not found. {}", e.getMessage(), e);
            sendResponse(exchange, 404, e.getMessage()); // Handle book not found errors
        } catch (InvalidBookException e) {

            // Handle invalid book data errors
            logger.error("Invalid book data. {}", e.getMessage(), e);
            sendResponse(exchange, 400, e.getMessage()); // Handle invalid book data errors
        } catch (IllegalArgumentException e) {

            // Handle invalid input errors
            logger.error("Invalid input. {}", e.getMessage(), e);
            sendResponse(exchange, 400, "Invalid input: " + e.getMessage()); // Handle invalid input errors
        } catch (Exception e) {

            // Handle unexpected errors
            logger.error("Internal server error. {}", e.getMessage(), e);
            sendResponse(exchange, 500, "Internal server error. " + e.getMessage()); // Handle unexpected errors
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

                // Send the list of books as JSON
                sendResponse(exchange, 200, objectMapper.writeValueAsString(books));

                // Log the successful retrieval of all books
                logger.info("Successfully retrieved all books");
                break;
            case "POST":
                // Parse the request body into a Book object
                Book book = parseRequestBody(exchange, Book.class);

                // Validate that required fields are not null or empty
                validateFieldsNotEmpty(
                        book.getTitle(), "Title",
                        book.getAuthor(), "Author",
                        book.getIsbn(), "ISBN",
                        book.getStatus() == null ? null : book.getStatus().name(), "Status");

                // Validate that the published date is not null
                if (book.getPublishedDate() == null) {
                    throw new InvalidBookException("Published date cannot be null.");
                }

                // Add the new book to the service
                bookService.addBook(book);

                // Send a success response
                sendResponse(exchange, 201, "Book added successfully");

                // Log the successful addition of the new book
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

                // Send the book details as JSON and log the success
                sendResponse(exchange, 200, objectMapper.writeValueAsString(book));
                logger.info("Successfully retrieved book with ID: {}", id);
                break;
            case "PATCH":

                // Parse the request body into a Book object
                Book updatedBook = parseRequestBody(exchange, Book.class);

                // Ensure the ID matches the path
                updatedBook.setId(id);

                // Update the book in the service
                bookService.updateBook(updatedBook);

                // Send a success response and log it
                sendResponse(exchange, 200, "Book updated successfully");
                logger.info("Successfully updated book with ID: {}", id);
                break;
            case "DELETE":

                // Delete the book by ID from the service
                bookService.deleteBook(id);

                // Send a success response and log it
                sendResponse(exchange, 204, "");
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
    private void handleSearchBooks(HttpExchange exchange, String query, String method) throws IOException {
        // Handle unsupported HTTP methods
        if (!method.equals("GET")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            logger.warn("Method not allowed for search books endpoint: {}", method);
            return;
        }

        // Parse the query parameters into a map
        Map<String, String> queryParams = parseQueryParameters(query);

        if (queryParams.containsKey("title")) {

            // Search books by title
            String title = queryParams.get("title");

            // Validate that the title is not empty
            validateFieldsNotEmpty(title, "Title");

            // Retrieve the list of books by title from the service
            List<Book> books = bookService.getBooksByTitle(title);

            // Send the list of books as JSON and log the success
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books));
            logger.info("Successfully searched books by title: {}", title);
        } else if (queryParams.containsKey("author")) {

            // Search books by author
            String author = queryParams.get("author");

            // Validate that the author is not empty
            validateFieldsNotEmpty(author, "Author");

            // Retrieve the list of books by author from the service
            List<Book> books = bookService.getBooksByAuthor(author);

            // Send the list of books as JSON and log the success
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books));
            logger.info("Successfully searched books by author: {}", author);
        } else if (queryParams.containsKey("isbn")) {

            // Search books by ISBN
            String isbn = queryParams.get("isbn");

            // Validate that the ISBN is not empty
            validateFieldsNotEmpty(isbn, "ISBN");

            // Retrieve the book by ISBN from the service
            Book book = bookService.getBookByIsbn(isbn);

            // Send the book details as JSON and log the success
            sendResponse(exchange, 200, objectMapper.writeValueAsString(book));
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
        // Handle unsupported HTTP methods
        if (!method.equals("PATCH")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            logger.warn("Method not allowed for search books endpoint: {}", method);
            return;
        }

        // Parse the request's status field into a JsonNode
        JsonNode statusNode = parseRequestBody(exchange, JsonNode.class).get("status");

        // Validate the "status" field exists and is not empty
        if (statusNode == null || statusNode.isNull() || statusNode.asText().isEmpty()) {
            throw new IllegalArgumentException("Status field is required.");
        }

        // Convert the status to a BookStatus enum type
        BookStatus bookStatus = BookStatus.valueOf(statusNode.asText().toUpperCase());

        // Update the book status in the service
        bookService.updateBookStatus(id, bookStatus);

        // Send a success response and log it
        sendResponse(exchange, 200, "Book status updated successfully");
        logger.info("Successfully updated status for book with ID: {}", id);
    }
}