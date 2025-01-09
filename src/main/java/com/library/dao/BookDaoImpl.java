package com.library.dao;

import com.library.model.Book;
import com.library.model.BookStatus;
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

            // Set the values of the prepared statement
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setDate(4, book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null);
            statement.setString(5, book.getStatus().name()); // Convert enum to string

            // Execute the statement, retrieve and set the generated ID
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

            // Set the value of the prepared statement
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Check if a book was found and return it
            if (resultSet.next()) {
                return mapResultSetToBook(resultSet); // Call the helper method to map the result set to a Book object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no book was found
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

                // Execute the query and get the result set
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {

                // Call the helper method to map the result set to a Book object
                books.add(mapResultSetToBook(resultSet));
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
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_date = ?, status = ? WHERE id = ?";

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
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the value of the prepared statement
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

            // Set the value of the prepared statement
            statement.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
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

            // Set the value of the prepared statement
            statement.setString(1, "%" + author.toLowerCase() + "%");

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each book to the list
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet)); // Call the helper method to map the result set to a Book
                                                          // object
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

            // Set the value of the prepared statement
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            // Check if a book was found and return it
            if (resultSet.next()) {
                return mapResultSetToBook(resultSet); // Call the helper method to map the result set to a Book object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no book was found
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
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                resultSet.getDate("published_date") != null
                        ? resultSet.getDate("published_date").toLocalDate()
                        : null,
                BookStatus.valueOf(resultSet.getString("status").toUpperCase()) // Convert string to enum and set status
        );
        book.setId(resultSet.getInt("id")); // Explicitly set the book's ID

        return book; // Return the Book object
    }
}