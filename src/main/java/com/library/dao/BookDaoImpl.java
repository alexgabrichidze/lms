package com.library.dao;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the BookDao interface.
 * Provides methods to perform CRUD operations on the books table in the
 * database.
 */
public class BookDaoImpl implements BookDao {

    // Logger for debugging and error logging
    private static final Logger logger = LoggerFactory.getLogger(BookDaoImpl.class);

    /**
     * Adds a new book to the database.
     *
     * @param book the Book object to be added
     */
    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, published_date, status) VALUES (?, ?, ?, ?, ?) RETURNING id";

        // Log the book being added
        logger.info("Attempting to add book: {}", book);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the values of the prepared statement
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null);
            statement.setString(5, book.getStatus().name()); // Convert enum to string

            // Execute the statement, retrieve and set the generated ID
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                // Set the ID of the book and log the success message
                book.setId(resultSet.getInt("id"));
                logger.info("Book added successfully with ID: {}", book.getId());
            }
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while adding book: {}", book);
            throw new RuntimeException("Failed to add book", e);
        }
    }

    /**
     * Retrieves a book by its ID from the database.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found, or null if no book with the given ID exists
     */
    @Override
    public Book getBookById(int id) {
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books WHERE id = ?";

        // Log the book being fetched
        logger.info("Fetching book with ID: {}", id);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the value of the prepared statement
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Check if a book was found and return it
            if (resultSet.next()) {

                // Call the helper method to map the result set to a Book object
                Book book = mapResultSetToBook(resultSet);

                // Log the success message and return the book
                logger.info("Book fetched successfully: {}", book);
                return book;
            } else {

                // Log the warning message if no book was found
                logger.warn("No book found with ID: {}", id);
                return null; // Return null if no book was found
            }
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while fetching book with ID: {}", id);
            throw new RuntimeException("Failed to fetch book", e);
        }
    }

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all Book objects, or an empty list if no books are found
     */
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books";

        // Log the books being fetched
        logger.info("Fetching all books.");

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

                // Execute the query and get the result set
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {

                // Call the helper method to map the result set to a Book object
                books.add(mapResultSetToBook(resultSet));
            }
            logger.info("Successfully fetched {} books.", books.size()); // Log the success message
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while fetching all books");
            throw new RuntimeException("Failed to fetch books", e);
        }
        return books; // Return the list of books
    }

    /**
     * Updates the details of an existing book in the database.
     *
     * @param book the Book object containing the updated details
     */
    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_date = ?, status = ? WHERE id = ?";

        // Log the book being updated
        logger.info("Updating book: {}", book);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the values of the prepared statement
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null);
            statement.setString(5, book.getStatus().name()); // Convert enum to string
            statement.setInt(6, book.getId());

            // Execute the statement
            int rowsUpdated = statement.executeUpdate();

            // Log the success message
            logger.info("Updated {} row(s) for book ID: {}", rowsUpdated, book.getId());
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while updating book: {}", book);
            throw new RuntimeException("Failed to update book", e);
        }
    }

    /**
     * Deletes a book from the database by its ID.
     *
     * @param id the ID of the book to delete
     */
    @Override
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        // Log the book being deleted
        logger.info("Attempting to delete book with ID: {}", id);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the value of the prepared statement
            statement.setInt(1, id);

            // Execute the statement and log the success message
            int rowsDeleted = statement.executeUpdate();
            logger.info("Deleted {} row(s) for book ID: {}", rowsDeleted, id);
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while deleting book with ID: {}", id);
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    /**
     * Retrieves books by their title from the database.
     *
     * @param title the title to search for (case-insensitive, partial matches
     *              allowed)
     * @return a list of books matching the title
     */
    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books WHERE LOWER(title) LIKE ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the books being fetched
            logger.info("Fetching books with title matching: {}", title);

            // Set the value of the prepared statement
            statement.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }

            // Log the success message
            logger.info("Successfully fetched {} book(s) matching title: {}", books.size(), title);
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while fetching books with title: {}", title);
            throw new RuntimeException("Failed to fetch books by title", e);
        }
        return books; // Return the list of books
    }

    /**
     * Retrieves books by their author from the database.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @return a list of books matching the author
     */
    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books WHERE LOWER(author) LIKE ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the books being fetched
            logger.info("Fetching books with author matching: {}", author);

            // Set the value of the prepared statement
            statement.setString(1, "%" + author.toLowerCase() + "%");

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }

            // Log the success message
            logger.info("Successfully fetched {} book(s) by author: {}", books.size(), author);
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while fetching books by author: {}", author);
            throw new RuntimeException("Failed to fetch books by author", e);
        }
        return books; // Return the list of books
    }

    /**
     * Retrieves a book by its ISBN from the database.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found, or null if no book with the given ISBN
     *         exists
     */
    @Override
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books WHERE isbn = ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the book being fetched
            logger.info("Fetching book with ISBN: {}", isbn);

            // Set the value of the prepared statement
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            // Check if a book was found and return it
            if (resultSet.next()) {

                // Call the helper method to map the result set to a Book object
                Book book = mapResultSetToBook(resultSet);

                // Log the success message and return the book
                logger.info("Book fetched successfully: {}", book);
                return book;
            } else {

                // Log the warning message if no book was found
                logger.warn("No book found with ISBN: {}", isbn);
                return null;
            }
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while fetching book with ISBN: {}", isbn);
            throw new RuntimeException("Failed to fetch book by ISBN", e);
        }
    }

    /**
     * Helper method to map a result set to a Book object.
     *
     * @param resultSet the result set to map
     * @return the Book object
     * @throws SQLException if a database access error occurs
     */
    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {

        // Create a new Book object and set its attributes
        Book book = new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                resultSet.getDate("published_date") != null
                        ? resultSet.getDate("published_date").toLocalDate()
                        : null,
                BookStatus.valueOf(resultSet.getString("status").toUpperCase()) // Convert string to enum and set status
        );

        // Log the User object mapped
        logger.debug("Mapped ResultSet to Book: {}", book);
        return book; // Return the Book object
    }
}