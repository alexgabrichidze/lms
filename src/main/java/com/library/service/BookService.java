package com.library.service;

import com.library.model.Book;
import com.library.service.exceptions.BookNotFoundException;
import com.library.service.exceptions.InvalidBookException;

import java.util.List;

/**
 * Service interface for managing books in the library system.
 * Defines business logic for operations related to books.
 */
public interface BookService {

    /**
     * Adds a new book to the library after validation.
     *
     * @param book the Book object to add
     * @throws InvalidBookException if the book data is invalid
     */
    void addBook(Book book);

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found
     * @throws BookNotFoundException if no book with the given ID exists
     */
    Book getBookById(int id);

    /**
     * Retrieves all books in the library.
     *
     * @return a list of all Book objects
     */
    List<Book> getAllBooks();

    /**
     * Updates the details of an existing book after validation.
     *
     * @param book the Book object with updated details
     * @throws InvalidBookException  if the updated book data is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    void updateBook(Book book);

    /**
     * Deletes a book by its unique ID.
     *
     * @param id the ID of the book to delete
     * @throws BookNotFoundException if no book with the given ID exists
     */
    void deleteBook(int id);

    /**
     * Searches for books by their title.
     *
     * @param title the title to search for (case-insensitive, partial matches
     *              allowed)
     * @return a list of books matching the title
     */
    List<Book> searchBooksByTitle(String title);

    /**
     * Searches for books by their author.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @return a list of books matching the author
     */
    List<Book> searchBooksByAuthor(String author);

    /**
     * Updates the status of a book (e.g., AVAILABLE, BORROWED, RESERVED).
     *
     * @param id     the ID of the book to update
     * @param status the new status of the book
     * @throws InvalidBookException  if the status is invalid
     * @throws BookNotFoundException if no book with the given ID exists
     */
    void updateBookStatus(int id, String status);
}
