package com.library.dao;

import com.library.model.Book;

import java.util.List;

/**
 * Interface for managing books in the database.
 * Provides methods for CRUD operations and search queries.
 */
public interface BookDao {

    /**
     * Adds a new book to the database.
     *
     * @param book the Book object to be added
     */
    void addBook(Book book);

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found, or null if no book with the given ID exists
     */
    Book getBookById(int id);

    /**
     * Retrieves all books in the database.
     *
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of all Book objects
     */
    List<Book> getAllBooks(int offset, int limit);

    /**
     * Updates the details of an existing book in the database.
     *
     * @param book the Book object containing the updated details
     */
    void updateBook(Book book);

    /**
     * Retrieves books by their title from the database.
     *
     * @param title  the title to search for (case-insensitive, partial matches
     *               allowed)
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of books matching the title
     */
    List<Book> getBooksByTitle(String title, int offset, int limit);

    /**
     * Retrieves books by their author from the database.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of books matching the author
     */
    List<Book> getBooksByAuthor(String author, int offset, int limit);

    /**
     * Retrieves a book by its ISBN from the database.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found, or null if no book with the given ISBN
     *         exists
     */
    Book getBookByIsbn(String isbn);

    /**
     * Deletes a book from the database by its ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteBook(int id);
}
