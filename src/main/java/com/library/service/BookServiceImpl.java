package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.BookDaoImpl;
import com.library.model.Book;
import com.library.model.BookStatus;

import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the BookService interface.
 * Handles business logic for book-related operations.
 */
public class BookServiceImpl implements BookService {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    // Data access object for books
    private final BookDao bookDao;

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
        logger.info("Attempting to add book: {}", book);

        // Validate that the ISBN is unique
        Book existingBook = bookDao.getBookByIsbn(book.getIsbn());
        if (existingBook != null) {
            throw new InvalidBookException("A book with the same ISBN already exists.");
        }

        // Validate that the status is valid (AVAILABLE or BORROWED)
        if (!Arrays.asList(BookStatus.values()).contains(book.getStatus())) {
            throw new InvalidBookException("Invalid book status.");
        }

        // Validate that the ISBN is 13 characters long and contains only numbers
        if (!book.getIsbn().matches("\\d{13}")) {
            throw new InvalidBookException("Invalid ISBN format.");
        }

        // Validate that the published date is not in the future
        if (book.getPublishedDate().isAfter(LocalDate.now())) {
            throw new InvalidBookException("Invalid published date.");
        }

        // Add the book to the database
        bookDao.addBook(book);
        logger.info("Book added successfully with ID: {}", book.getId());
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found
     */
    @Override
    public Book getBookById(int id) {

        logger.info("Fetching book with ID: {}", id); // Log the fetch operation

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
        logger.info("Fetching all books.");

        // Fetch all books
        List<Book> books = bookDao.getAllBooks();

        // Log the count of books fetched and return the list
        logger.info("Successfully fetched {} books.", books.size());
        return books;
    }

    /**
     * Updates the details of an existing book.
     *
     * @param book the Book object with updated details
     */
    @Override
    public void updateBook(Book book) {
        logger.info("Updating book: {}", book); // Log the update attempt

        // Check if the book exists before proceeding
        Book existingBook = bookDao.getBookById(book.getId());

        // If book is not found, log a warning and throw an exception
        if (existingBook == null) {
            logger.warn("Book with ID {} not found for update.", book.getId());
            throw new BookNotFoundException("Book with ID " + book.getId() + " not found.");
        }

        // Merge updated fields into the existing book
        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getIsbn() != null) {
            // Validate that the ISBN is 13 characters long and contains only numbers
            if (!book.getIsbn().matches("\\d{13}")) {
                throw new InvalidBookException("Invalid ISBN format.");
            }

            // Validate that the ISBN is unique (if updated)
            if (!book.getIsbn().equals(existingBook.getIsbn())) {
                Book bookWithSameIsbn = bookDao.getBookByIsbn(book.getIsbn());
                if (bookWithSameIsbn != null) {
                    throw new InvalidBookException("A book with the same ISBN already exists.");
                }
            }

            existingBook.setIsbn(book.getIsbn());
        }
        if (book.getPublishedDate() != null) {
            // Validate that the published date is not in the future
            if (book.getPublishedDate().isAfter(LocalDate.now())) {
                throw new InvalidBookException("Invalid published date.");
            }

            existingBook.setPublishedDate(book.getPublishedDate());
        }
        if (book.getStatus() != null) {
            // Validate that the status is valid (if updated)
            if (!Arrays.asList(BookStatus.values()).contains(book.getStatus())) {
                throw new InvalidBookException("Invalid book status.");
            }

            existingBook.setStatus(book.getStatus());
        }

        bookDao.updateBook(existingBook);
        logger.info("Book updated successfully: {}", existingBook); // Log success
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
        logger.info("Attempting to delete book with ID: {}", id);

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
        logger.info("Fetching books with title: {}", title);

        List<Book> books = bookDao.getBooksByTitle(title); // Fetch books by title

        // If no books are found, log a warning and return an empty list
        if (books.isEmpty()) {
            logger.warn("No books found for title: {}", title);
            throw new BookNotFoundException("No books found for title: " + title);
        }

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
        logger.info("Fetching books with author: {}", author);

        List<Book> books = bookDao.getBooksByAuthor(author); // Fetch books by author

        // If no books are found, log a warning and return an empty list
        if (books.isEmpty()) {
            logger.warn("No books found for author: {}", author);
            throw new BookNotFoundException("No books found for author: " + author);
        }

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
        logger.info("Fetching book with ISBN: {}", isbn);

        // Validate that the ISBN is 13 characters long and contains only numbers
        if (!isbn.matches("\\d{13}")) {
            throw new InvalidBookException("Invalid ISBN format.");
        }

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
        logger.info("Attempting to update status for book ID: {} to {}", id, status);

        // Check that the status is valid
        if (!Arrays.asList(BookStatus.values()).contains(status)) {
            throw new InvalidBookException("Invalid book status.");
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
