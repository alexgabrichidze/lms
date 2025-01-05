package com.library.dao;

import com.library.model.Book;
import com.library.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the BookDao interface.
 * Provides methods to perform CRUD operations on the books table in the
 * database.
 */
public class BookDaoImpl implements BookDao {

    /**
     * Adds a new book to the database.
     *
     * @param book the Book object to be added
     */
    @Override
    public void addBook(Book book) {
        // SQL query to insert a new record into the books table
        String sql = "INSERT INTO books (title, author, isbn, published_date, status) VALUES (?, ?, ?, ?, ?)";
        try (
                // Get a database connection
                Connection connection = ConnectionManager.getConnection();
                // Prepare the SQL query for execution
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders (?)
            statement.setString(1, book.getTitle()); // Set the title
            statement.setString(2, book.getAuthor()); // Set the author
            statement.setString(3, book.getIsbn()); // Set the ISBN
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null); // Set
                                                                                                                  // the
                                                                                                                  // published
                                                                                                                  // date
            statement.setString(5, book.getStatus()); // Set the status

            // Execute the query and insert the book into the database
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
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
        // SQL query to select a book by its ID
        String sql = "SELECT * FROM books WHERE id = ?";
        try (
                // Get a database connection
                Connection connection = ConnectionManager.getConnection();
                // Prepare the SQL query for execution
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the ID parameter in the query
            statement.setInt(1, id);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // If a result is found, create and return a Book object
            if (resultSet.next()) {
                return new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("published_date") != null ? resultSet.getDate("published_date").toLocalDate()
                                : null,
                        resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }
        return null; // Return null if no book is found
    }

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all Book objects, or an empty list if no books are found
     */
    @Override
    public List<Book> getAllBooks() {
        // List to store the retrieved books
        List<Book> books = new ArrayList<>();
        // SQL query to select all books
        String sql = "SELECT * FROM books";
        try (
                // Get a database connection
                Connection connection = ConnectionManager.getConnection();
                // Prepare the SQL query for execution
                PreparedStatement statement = connection.prepareStatement(sql);
                // Execute the query and get the result set
                ResultSet resultSet = statement.executeQuery()) {
            // Iterate through the result set and add each book to the list
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("published_date") != null ? resultSet.getDate("published_date").toLocalDate()
                                : null,
                        resultSet.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
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
        // SQL query to update a book's details by its ID
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_date = ?, status = ? WHERE id = ?";
        try (
                // Get a database connection
                Connection connection = ConnectionManager.getConnection();
                // Prepare the SQL query for execution
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders (?)
            statement.setString(1, book.getTitle()); // Set the updated title
            statement.setString(2, book.getAuthor()); // Set the updated author
            statement.setString(3, book.getIsbn()); // Set the updated ISBN
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null); // Set
                                                                                                                  // the
                                                                                                                  // updated
                                                                                                                  // published
                                                                                                                  // date
            statement.setString(5, book.getStatus()); // Set the updated status
            statement.setInt(6, book.getId()); // Set the ID of the book to update

            // Execute the query and update the book in the database
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }
    }

    /**
     * Deletes a book from the database by its ID.
     *
     * @param id the ID of the book to delete
     */
    @Override
    public void deleteBook(int id) {
        // SQL query to delete a book by its ID
        String sql = "DELETE FROM books WHERE id = ?";
        try (
                // Get a database connection
                Connection connection = ConnectionManager.getConnection();
                // Prepare the SQL query for execution
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the ID parameter in the query
            statement.setInt(1, id);

            // Execute the query and delete the book from the database
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }
    }
}
