package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.BookDaoImpl;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;
import com.library.util.ValidationUtil;

import java.util.List;

/**
 * Implementation of the BookService interface.
 * Handles business logic for book-related operations.
 */
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    /**
     * Constructor to initialize the BookDao implementation.
     */
    public BookServiceImpl() {
        this.bookDao = new BookDaoImpl();
    }

    /**
     * Adds a new book to the library after validation.
     *
     * @param book the Book object to add
     * @throws InvalidBookException if the book data is invalid
     */
    @Override
    public void addBook(Book book) {
        if (book == null) {
            throw new InvalidBookException("Book cannot be null.");
        }

        // Validate book fields before adding
        ValidationUtil.validateNotEmpty(book.getTitle(), "Book title",
                () -> new InvalidBookException("Book title cannot be null or empty."));
        ValidationUtil.validateNotEmpty(book.getAuthor(), "Book author",
                () -> new InvalidBookException("Book author cannot be null or empty."));
        ValidationUtil.validateNotEmpty(book.getIsbn(), "Book ISBN",
                () -> new InvalidBookException("Book ISBN cannot be null or empty."));

        // Use BookStatus enum for default status
        if (book.getStatus() == null) {
            book.setStatus(BookStatus.AVAILABLE);
        }

        bookDao.addBook(book);
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found
     * @throws InvalidBookException  if the ID is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    @Override
    public Book getBookById(int id) {
        if (id <= 0) {
            throw new InvalidBookException("Book ID must be a positive integer.");
        }

        // Retrieve book and check existence
        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        return book;
    }

    /**
     * Retrieves all books in the library.
     *
     * @return a list of all Book objects
     */
    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    /**
     * Updates the details of an existing book after validation.
     *
     * @param book the Book object with updated details
     * @throws InvalidBookException  if the book data is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    @Override
    public void updateBook(Book book) {
        if (book == null || book.getId() <= 0) {
            throw new InvalidBookException("Invalid book ID.");
        }

        // Check if the book exists before proceeding
        Book existingBook = bookDao.getBookById(book.getId());
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + book.getId() + " not found.");
        }

        // Validate fields if updated
        if (book.getTitle() != null) {
            ValidationUtil.validateNotEmpty(book.getTitle(), "Book title",
                    () -> new InvalidBookException("Book title cannot be empty."));
        }

        if (book.getAuthor() != null) {
            ValidationUtil.validateNotEmpty(book.getAuthor(), "Book author",
                    () -> new InvalidBookException("Book author cannot be empty."));
        }

        if (book.getIsbn() != null) {
            ValidationUtil.validateNotEmpty(book.getIsbn(), "Book ISBN",
                    () -> new InvalidBookException("Book ISBN cannot be empty."));
        }

        bookDao.updateBook(book);
    }

    /**
     * Deletes a book by its unique ID.
     *
     * @param id the ID of the book to delete
     * @throws InvalidBookException  if the ID is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    @Override
    public void deleteBook(int id) {
        if (id <= 0) {
            throw new InvalidBookException("Book ID must be a positive integer.");
        }

        // Check if the book exists before proceeding
        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        bookDao.deleteBook(id);
    }

    /**
     * Retrieves books by their title.
     *
     * @param title the title to search for (case-insensitive, partial matches
     *              allowed)
     * @return a list of books matching the title
     * @throws InvalidBookException if the title is null or empty
     */
    @Override
    public List<Book> getBooksByTitle(String title) {
        ValidationUtil.validateNotEmpty(title, "Book title",
                () -> new InvalidBookException("Book title cannot be null or empty."));

        return bookDao.getBooksByTitle(title);
    }

    /**
     * Retrieves books by their author.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @return a list of books matching the author
     * @throws InvalidBookException if the author is null or empty
     */
    @Override
    public List<Book> getBooksByAuthor(String author) {
        ValidationUtil.validateNotEmpty(author, "Book author",
                () -> new InvalidBookException("Book author cannot be null or empty."));

        return bookDao.getBooksByAuthor(author);
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found
     * @throws InvalidBookException  if the ISBN is null or empty
     * @throws BookNotFoundException if no book with the given ISBN exists
     */
    @Override
    public Book getBookByIsbn(String isbn) {
        ValidationUtil.validateNotEmpty(isbn, "Book ISBN",
                () -> new InvalidBookException("Book ISBN cannot be null or empty."));

        // Check if the book exists before proceeding
        Book book = bookDao.getBookByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
        }

        return book;
    }

    /**
     * Updates the status of a book (e.g., AVAILABLE, BORROWED).
     *
     * @param id     the ID of the book to update
     * @param status the new status of the book
     * @throws InvalidBookException  if the ID or status is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    @Override
    public void updateBookStatus(int id, BookStatus status) {
        if (id <= 0) {
            throw new InvalidBookException("Book ID must be a positive integer.");
        }

        // Validate book status before updating
        if (status == null) {
            throw new InvalidBookException("Book status cannot be null.");
        }

        // Check if the book exists before proceeding
        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        book.setStatus(status);
        bookDao.updateBook(book);
    }
}
