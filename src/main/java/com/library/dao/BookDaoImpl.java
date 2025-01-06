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
        String sql = "INSERT INTO books (title, author, isbn, published_date, status) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the insert query
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null);
            statement.setString(5, book.getStatus());

            // Execute the insert query and retrieve the generated ID
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book.setId(resultSet.getInt("id")); // Set the generated ID in the Book object
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id); // Set the id parameter for the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Map the result set to a Book object
                Book book = new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("published_date") != null
                                ? resultSet.getDate("published_date").toLocalDate()
                                : null,
                        resultSet.getString("status"));
                book.setId(resultSet.getInt("id")); // Set the id field explicitly
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, published_date, status FROM books";
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Create a Book object and set all its fields, including the ID
                Book book = new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("published_date") != null
                                ? resultSet.getDate("published_date").toLocalDate()
                                : null,
                        resultSet.getString("status"));
                book.setId(resultSet.getInt("id")); // Set the book's ID
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
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