package com.library.dao;

import com.library.model.Book;
import java.util.List;

/**
 * Data Access Object (DAO) interface for managing Book entities.
 * Provides methods to perform CRUD (Create, Read, Update, Delete) operations on
 * the books table.
 */
public interface BookDao {

    /**
     * Adds a new book to the database.
     *
     * @param book the Book object to be added
     * @throws IllegalArgumentException if the book object is null or contains
     *                                  invalid data
     */
    void addBook(Book book);

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found, or null if no book with the given ID exists
     */
    Book getBookById(int id);

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all Book objects in the database, or an empty list if no
     *         books are found
     */
    List<Book> getAllBooks();

    /**
     * Updates the details of an existing book in the database.
     *
     * @param book the Book object containing updated information
     * @throws IllegalArgumentException if the book object is null or contains
     *                                  invalid data
     */
    void updateBook(Book book);

    /**
     * Deletes a book from the database by its ID.
     *
     * @param id the ID of the book to delete
     * @throws IllegalArgumentException if the ID is invalid (e.g., negative)
     */
    void deleteBook(int id);
}
