package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.BookDaoImpl;
import com.library.model.Book;
import com.library.model.BookStatus;

import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.library.util.ValidationUtil.*;

import java.util.List;

/**
 * Implementation of the BookService interface.
 * Handles business logic for book-related operations.
 */
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class); // Logger for this class

    private final BookDao bookDao; // Data access object for books

    /**
     * Constructor to initialize the BookDao implementation.
     */
    public BookServiceImpl() {
        this.bookDao = new BookDaoImpl(); // Default implementation
    }

    /**
     * Constructor to initialize BookService with custom BookDao class.
     *
     * @param bookDao the custom BookDao implementation to use
     */
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao; // Custom implementation
    }

    /**
     * Adds a new book to the library after validation.
     *
     * @param book the Book object to add
     */
    @Override
    public void addBook(Book book) {

        logger.debug("Attempting to add book: {}", book); // Log the attempt to add a book

        // Validate book object
        if (book == null) {
            throw new InvalidBookException("Book cannot be null.");
        }

        // Validate book fields before adding
        validateNotEmpty(book.getTitle(), "Book title",
                () -> new InvalidBookException("Book title cannot be null or empty."));
        validateNotEmpty(book.getAuthor(), "Book author",
                () -> new InvalidBookException("Book author cannot be null or empty."));
        validateNotEmpty(book.getIsbn(), "Book ISBN",
                () -> new InvalidBookException("Book ISBN cannot be null or empty."));

        // Use BookStatus enum for default status
        if (book.getStatus() == null) {
            book.setStatus(BookStatus.AVAILABLE);
        }

        bookDao.addBook(book); // Add the book
        logger.info("Book added successfully with ID: {}", book.getId()); // Log success
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found
     */
    @Override
    public Book getBookById(int id) {

        logger.debug("Fetching book with ID: {}", id); // Log the fetch operation

        // Validate book ID (must be positive)
        validatePositiveId(id, "Book ID",
                () -> new InvalidBookException("Book ID must be a positive integer."));

        // Retrieve book and check existence
        Book book = bookDao.getBookById(id);

        // If book is not found, log a warning and throw an exception
        if (book == null) {
            logger.warn("Book with ID {} not found.", id); // Log a warning for not found
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        logger.info("Book fetched successfully: {}", book); // Log success
        return book; // Return the book if found
    }

    /**
     * Retrieves all books in the library.
     *
     * @return a list of all Book objects
     */
    @Override
    public List<Book> getAllBooks() {

        // Log the fetch operation
        logger.debug("Fetching all books.");

        // Fetch all books
        List<Book> books = bookDao.getAllBooks();

        // Log the count of books fetched and return the list
        logger.info("Successfully fetched {} books.", books.size());
        return books;
    }

    /**
     * Updates the details of an existing book after validation.
     *
     * @param book the Book object with updated details
     */
    @Override
    public void updateBook(Book book) {

        logger.debug("Updating book: {}", book); // Log the update attempt

        // Validate book ID (must be positive and not null)
        if (book == null || book.getId() <= 0) {
            throw new InvalidBookException("Invalid book ID.");
        }

        // Check if the book exists before proceeding
        Book existingBook = bookDao.getBookById(book.getId());

        // If book is not found, log a warning and throw an exception
        if (existingBook == null) {
            logger.warn("Book with ID {} not found for update.", book.getId());
            throw new BookNotFoundException("Book with ID " + book.getId() + " not found.");
        }

        // Validate fields title, author, and ISBN, if updated
        if (book.getTitle() != null) {
            validateNotEmpty(book.getTitle(), "Book title",
                    () -> new InvalidBookException("Book title cannot be empty."));
        }

        if (book.getAuthor() != null) {
            validateNotEmpty(book.getAuthor(), "Book author",
                    () -> new InvalidBookException("Book author cannot be empty."));
        }

        if (book.getIsbn() != null) {
            validateNotEmpty(book.getIsbn(), "Book ISBN",
                    () -> new InvalidBookException("Book ISBN cannot be empty."));
        }

        bookDao.updateBook(book); // Update the book
        logger.info("Book updated successfully: {}", book); // Log success
    }

    /**
     * Deletes a book by its unique ID.
     *
     * @param id the ID of the book to delete
     * 
     */
    @Override
    public void deleteBook(int id) {

        // Log the delete attempt
        logger.debug("Attempting to delete book with ID: {}", id);

        // Validate book ID (must be positive)
        validatePositiveId(id, "Book ID",
                () -> new InvalidBookException("Book ID must be a positive integer."));

        // Check if the book exists before proceeding
        Book book = bookDao.getBookById(id);

        // If book is not found, log a warning and throw an exception
        if (book == null) {
            logger.warn("Book with ID {} not found for deletion.", id);
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        bookDao.deleteBook(id); // Delete the book
        logger.info("Book with ID {} deleted successfully.", id); // Log success
    }

    /**
     * Retrieves books by their title.
     *
     * @param title the title to search for (case-insensitive, partial matches
     *              allowed)
     * @return a list of books matching the title
     */
    @Override
    public List<Book> getBooksByTitle(String title) {

        // Log the fetch attempt
        logger.debug("Fetching books with title: {}", title);

        // Validate book title before fetching the list of books
        validateNotEmpty(title, "Book title",
                () -> new InvalidBookException("Book title cannot be null or empty."));

        List<Book> books = bookDao.getBooksByTitle(title); // Fetch books by title

        // Log the success and return the list
        logger.info("Successfully fetched {} book(s) matching title: {}", books.size(), title);
        return books;
    }

    /**
     * Retrieves books by their author.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @return a list of books matching the author
     */
    @Override
    public List<Book> getBooksByAuthor(String author) {

        // Log the fetch attempt
        logger.debug("Fetching books with author: {}", author);

        // Validate book author before fetching the list of books
        validateNotEmpty(author, "Book author",
                () -> new InvalidBookException("Book author cannot be null or empty."));

        List<Book> books = bookDao.getBooksByAuthor(author); // Fetch books by author

        // Log the success and return the list
        logger.info("Successfully fetched {} book(s) by author: {}", books.size(), author);
        return books;
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found
     */
    @Override
    public Book getBookByIsbn(String isbn) {

        // Log the fetch attempt
        logger.debug("Fetching book with ISBN: {}", isbn);

        // Validate book ISBN before fetching the book
        validateNotEmpty(isbn, "Book ISBN",
                () -> new InvalidBookException("Book ISBN cannot be null or empty."));

        // Check if the book exists before proceeding
        Book book = bookDao.getBookByIsbn(isbn);

        // If book is not found, log a warning and throw an exception
        if (book == null) {
            logger.warn("Book with ISBN {} not found.", isbn);
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
        }

        // Log the success and return the book
        logger.info("Book fetched successfully: {}", book);
        return book;
    }

    /**
     * Updates the status of a book (e.g., AVAILABLE, BORROWED).
     *
     * @param id     the ID of the book to update
     * @param status the new status of the book
     */
    @Override
    public void updateBookStatus(int id, BookStatus status) {

        // Log the update attempt
        logger.debug("Attempting to update status for book ID: {} to {}", id, status);

        // Validate book ID (must be positive)
        validatePositiveId(id, "Book ID",
                () -> new InvalidBookException("Book ID must be a positive integer."));

        // Ensure the status is not null before updating
        if (status == null) {
            logger.warn("Book status is null for book ID: {}", id);
            throw new InvalidBookException("Book status cannot be null.");
        }

        // Check if the book exists before proceeding
        Book book = bookDao.getBookById(id);

        // If book is not found, log a warning and throw an exception
        if (book == null) {
            logger.warn("Book with ID {} not found for status update.", id); // Log warning for not found
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        book.setStatus(status); // Update the book status
        bookDao.updateBook(book); // Update the book

        logger.info("Book status updated successfully for ID: {} to {}", id, status); // Log success
    }
}
