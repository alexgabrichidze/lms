package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.BookDaoImpl;
import com.library.model.Book;
import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;

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
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidBookException("Book title cannot be null or empty.");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new InvalidBookException("Book author cannot be null or empty.");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidBookException("Book ISBN cannot be null or empty.");
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

        Book existingBook = bookDao.getBookById(book.getId());
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + book.getId() + " not found.");
        }

        if (book.getTitle() != null && book.getTitle().trim().isEmpty()) {
            throw new InvalidBookException("Book title cannot be empty.");
        }
        if (book.getAuthor() != null && book.getAuthor().trim().isEmpty()) {
            throw new InvalidBookException("Book author cannot be empty.");
        }
        if (book.getIsbn() != null && book.getIsbn().trim().isEmpty()) {
            throw new InvalidBookException("Book ISBN cannot be empty.");
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
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidBookException("Book title cannot be null or empty.");
        }
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
        if (author == null || author.trim().isEmpty()) {
            throw new InvalidBookException("Book author cannot be null or empty.");
        }
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
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new InvalidBookException("Book ISBN cannot be null or empty.");
        }
        Book book = bookDao.getBookByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
        }
        return book;
    }

    /**
     * Updates the status of a book (e.g., AVAILABLE, BORROWED, RESERVED).
     *
     * @param id     the ID of the book to update
     * @param status the new status of the book
     * @throws InvalidBookException  if the ID or status is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    @Override
    public void updateBookStatus(int id, String status) {
        if (id <= 0) {
            throw new InvalidBookException("Book ID must be a positive integer.");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidBookException("Book status cannot be null or empty.");
        }

        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        book.setStatus(status);
        bookDao.updateBook(book);
    }
}
