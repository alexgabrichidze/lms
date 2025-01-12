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

public class BookController extends BaseController {
    private final BookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            logger.info("Received {} request for path: {}", method, path);

            if (path.matches("/books")) {
                if (query != null) {
                    handleSearchBooks(exchange, query);
                } else {
                    handleBooksEndpoint(exchange, method); // Handle /books endpoint
                }
            } else if (path.matches("/books/\\d+")) {
                int id = extractIdFromPath(path);
                handleBookEndpoint(exchange, method, id); // Handle /books/{id} endpoint
            } else if (path.matches("/books/\\d+/status")) {
                int id = extractIdFromPath(path);
                handleBookStatusEndpoint(exchange, method, id);
            } else {
                sendResponse(exchange, 404, "Not Found");
            }
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage());
            sendResponse(exchange, 404, e.getMessage());
        } catch (InvalidBookException e) {
            logger.error("Invalid book data: {}", e.getMessage());
            sendResponse(exchange, 400, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            sendResponse(exchange, 400, "Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage(), e);
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleBooksEndpoint(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                List<Book> books = bookService.getAllBooks();
                sendResponse(exchange, 200, objectMapper.writeValueAsString(books));
                logger.info("Successfully retrieved all books");
                break;
            case "POST":
                Book book = parseRequestBody(exchange, Book.class);
                bookService.addBook(book);
                sendResponse(exchange, 201, "Book added successfully");
                logger.info("Successfully added book with ID: {}", book.getId());
                break;
            default:
                sendResponse(exchange, 405, "Method not allowed");
                logger.warn("Method not allowed for /books endpoint: {}", method);
        }
    }

    private void handleBookEndpoint(HttpExchange exchange, String method, int id) throws IOException {
        switch (method) {
            case "GET":
                Book book = bookService.getBookById(id);
                sendResponse(exchange, 200, objectMapper.writeValueAsString(book));
                logger.info("Successfully retrieved book with ID: {}", id);
                break;
            case "PUT":
                Book updatedBook = parseRequestBody(exchange, Book.class);
                updatedBook.setId(id);
                bookService.updateBook(updatedBook);
                sendResponse(exchange, 200, "Book updated successfully");
                logger.info("Successfully updated book with ID: {}", id);
                break;
            case "DELETE":
                bookService.deleteBook(id);
                sendResponse(exchange, 204, "");
                logger.info("Successfully deleted book with ID: {}", id);
                break;
            default:
                sendResponse(exchange, 405, "Method not allowed");
                logger.warn("Method not allowed for book endpoint: {}", method);
        }
    }

    private void handleSearchBooks(HttpExchange exchange, String query) throws IOException {
        Map<String, String> queryParams = parseQueryParameters(query);

        if (queryParams.containsKey("title")) {
            String title = queryParams.get("title");
            List<Book> books = bookService.getBooksByTitle(title);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books));
            logger.info("Successfully searched books by title: {}", title);
        } else if (queryParams.containsKey("author")) {
            String author = queryParams.get("author");
            List<Book> books = bookService.getBooksByAuthor(author);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(books));
            logger.info("Successfully searched books by author: {}", author);
        } else if (queryParams.containsKey("isbn")) {
            String isbn = queryParams.get("isbn");
            Book book = bookService.getBookByIsbn(isbn);
            sendResponse(exchange, 200, objectMapper.writeValueAsString(book));
            logger.info("Successfully searched book by ISBN: {}", isbn);
        } else {
            sendResponse(exchange, 400, "Invalid search parameter");
            logger.warn("Invalid search parameter: {}", query);
        }
    }

    private void handleBookStatusEndpoint(HttpExchange exchange, String method, int id) throws IOException {
        if (method.equals("PUT")) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String status = objectMapper.readValue(requestBody, String.class); // Parse status from request body

            try {
                BookStatus bookStatus = BookStatus.valueOf(status);
                bookService.updateBookStatus(id, bookStatus);
                sendResponse(exchange, 200, "Book status updated successfully");
                logger.info("Successfully updated status for book with ID: {}", id);
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, "Invalid book status: " + status);
                logger.warn("Invalid book status: {}", status);
            }
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
            logger.warn("Method not allowed for book status endpoint: {}", method);
        }
    }

    private Map<String, String> parseQueryParameters(String query) {
        return Stream.of(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(
                        arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8),
                        arr -> URLDecoder.decode(arr.length > 1 ? arr[1] : "", StandardCharsets.UTF_8)));
    }
}
