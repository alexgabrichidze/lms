package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.util.PaginatedResponse;

/**
 * Service interface for managing books in the library system.
 * Defines business logic for operations related to books.
 */
public interface BookService {

    /**
     * Adds a new book to the library after validation.
     *
     * @param book the Book object to add
     */
    void addBook(Book book);

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found
     */
    Book getBookById(int id);

    /**
     * Retrieves all books in the library with pagination.
     *
     * @param page the page number (zero-based)
     * @param size the number of books per page
     * @return a paginated response containing books and metadata
     */
    PaginatedResponse<Book> getAllBooks(int page, int size);

    /**
     * Updates the details of an existing book after validation.
     *
     * @param book the Book object with updated details
     */
    void updateBook(Book book);

    /**
     * Deletes a book by its unique ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteBook(int id);

    /**
     * Retrieves books by their title with pagination.
     *
     * @param title the title to search for (case-insensitive, partial matches
     *              allowed)
     * @param page  the page number (zero-based)
     * @param size  the number of books per page
     * @return a paginated response containing books and metadata
     */
    PaginatedResponse<Book> getBooksByTitle(String title, int page, int size);

    /**
     * Retrieves books by their author with pagination.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @param page   the page number (zero-based)
     * @param size   the number of books per page
     * @return a paginated response containing books and metadata
     */
    PaginatedResponse<Book> getBooksByAuthor(String author, int page, int size);

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found
     */
    Book getBookByIsbn(String isbn);

    /**
     * Updates the status of a book (e.g., AVAILABLE, BORROWED).
     *
     * @param id     the ID of the book to update
     * @param status the new status of the book
     */
    void updateBookStatus(int id, BookStatus status);

}
