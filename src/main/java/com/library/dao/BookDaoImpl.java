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

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(BookDaoImpl.class);

    /**
     * Adds a new book to the books table.
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
            statement.setDate(4, Date.valueOf(book.getPublishedDate()));
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
            throw new RuntimeException("Failed to add book.", e);
        }
    }

    /**
     * Retrieves a book by its ID from the books table.
     *
     * @param id the ID of the book to retrieve
     * @return the Book object if found, or null if no book with the given ID exists
     */
    @Override
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";

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
            throw new RuntimeException("Failed to fetch book.", e);
        }
    }

    /**
     * Retrieves all books from the books table.
     *
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of all Book objects, or an empty list if no books are found
     */
    @Override
    public List<Book> getAllBooks(int offset, int limit) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id LIMIT ? OFFSET ?";

        // Log the books being fetched
        logger.info("Fetching all books.");

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the values of the prepared statement
            statement.setInt(1, limit);
            statement.setInt(2, offset);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                // Call the helper method to map the result set to a Book object
                books.add(mapResultSetToBook(resultSet));
            }

            // Log success and return
            logger.info("Successfully fetched {} books.", books.size());
            return books;
        } catch (SQLException e) {
            // Log error and throw runtime exception
            logger.error("Error while fetching all books");
            throw new RuntimeException("Failed to fetch books.", e);
        }
    }

    /**
     * Updates an existing book in the books table.
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
            statement.setDate(4, Date.valueOf(book.getPublishedDate()));
            statement.setString(5, book.getStatus().name()); // Convert enum to string
            statement.setInt(6, book.getId());

            // Execute the statement
            int rowsUpdated = statement.executeUpdate();

            // Log the success message
            logger.info("Updated {} row(s) for book ID: {}", rowsUpdated, book.getId());
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while updating book: {}", book);
            throw new RuntimeException("Failed to update book.", e);
        }
    }

    /**
     * Deletes a book by its ID from the books table.
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

            // Set values of prepared statement
            statement.setInt(1, id);

            // Execute the statement and log the success message
            int rowsDeleted = statement.executeUpdate();
            logger.info("Deleted {} row(s) for book ID: {}", rowsDeleted, id);
        } catch (SQLException e) {

            // Log the error message and throw a runtime exception
            logger.error("Error while deleting book with ID: {}", id);
            throw new RuntimeException("Failed to delete book.", e);
        }
    }

    /**
     * Retrieves books by their title from the books table.
     *
     * @param title  the title to search for (case-insensitive, partial matches
     *               allowed)
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of books matching the title
     */
    @Override
    public List<Book> getBooksByTitle(String title, int offset, int limit) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? ORDER BY id LIMIT ? OFFSET ?";

        // Log the books being fetched
        logger.info("Fetching books with title matching: {}", title);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set values of prepared statement
            statement.setString(1, "%" + title.toLowerCase() + "%");
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }

            // Log success and return
            logger.info("Successfully fetched {} book(s) matching title: {}", books.size(), title);
            return books;
        } catch (SQLException e) {
            // Log error and throw runtime exception
            logger.error("Error while fetching books with title: {}", title);
            throw new RuntimeException("Failed to fetch books by title.", e);
        }
    }

    /**
     * Retrieves books by their author from the books table.
     *
     * @param author the author to search for (case-insensitive, partial matches
     *               allowed)
     * @param offset the number of books to skip
     * @param limit  the maximum number of books to retrieve
     * @return a list of books matching the author
     */
    @Override
    public List<Book> getBooksByAuthor(String author, int offset, int limit) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) LIKE ? ORDER BY id LIMIT ? OFFSET ?";

        // Log the books being fetched
        logger.info("Fetching books with author matching: {}", author);

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set values of prepared statement and execute statement
            statement.setString(1, "%" + author.toLowerCase() + "%");
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }

            // Log success and return
            logger.info("Successfully fetched {} book(s) by author: {}", books.size(), author);
            return books;
        } catch (SQLException e) {
            // Log error and throw runtime exception
            logger.error("Error while fetching books by author: {}", author);
            throw new RuntimeException("Failed to fetch books by author.", e);
        }
    }

    /**
     * Retrieves a book by its ISBN from the books table.
     *
     * @param isbn the ISBN to search for
     * @return the Book object if found, or null if no book with the given ISBN
     *         exists
     */
    @Override
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

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
            throw new RuntimeException("Failed to fetch book by ISBN.", e);
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
                resultSet.getDate("published_date").toLocalDate(),
                BookStatus.valueOf(resultSet.getString("status").toUpperCase()));

        logger.debug("Mapped ResultSet to Book: {}", book);
        return book;
    }
}