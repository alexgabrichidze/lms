package com.library.dao;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Test class for BookDaoImpl.
 * Ensures correct functionality of CRUD operations for books.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDaoImplTest {
    private BookDao bookDao;
    private int testBookId;

    /**
     * Sets up the test environment before all tests run.
     */
    @BeforeAll
    void setUp() {
        bookDao = new BookDaoImpl(); // Initialize the DAO
    }

    /**
     * Tests adding a new book to the database.
     */
    @Test
    @Order(1)
    void testAddBook() {
        Book book = new Book("1984", "George Orwell", "1234567890123", LocalDate.of(1949, 6, 8), BookStatus.AVAILABLE);
        bookDao.addBook(book);

        // Verify the book was added by fetching all books
        List<Book> books = bookDao.getAllBooks();
        assertTrue(books.size() > 0, "At least one book should exist after adding");
        testBookId = books.get(books.size() - 1).getId(); // Capture the ID of the last added book
    }

    /**
     * Tests retrieving a book by its unique ID.
     */
    @Test
    @Order(2)
    void testGetBookById() {
        Book book = bookDao.getBookById(testBookId);
        assertNotNull(book, "Book should be found by ID");
        assertEquals("1984", book.getTitle(), "Title should match");
        assertEquals("George Orwell", book.getAuthor(), "Author should match");
    }

    /**
     * Tests updating an existing book's details.
     */
    @Test
    @Order(3)
    void testUpdateBook() {
        Book book = bookDao.getBookById(testBookId);
        assertNotNull(book, "Book should exist for updating");

        // Update the book details
        book.setTitle("Animal Farm");
        book.setAuthor("George Orwell");
        book.setStatus(BookStatus.BORROWED);
        bookDao.updateBook(book);

        // Fetch the updated book and verify the changes
        Book updatedBook = bookDao.getBookById(testBookId);
        assertEquals("Animal Farm", updatedBook.getTitle(), "Updated title should match");
        assertEquals(BookStatus.BORROWED, updatedBook.getStatus(), "Updated status should match");
    }

    /**
     * Tests retrieving all books from the database.
     */
    @Test
    @Order(4)
    void testGetAllBooks() {
        List<Book> books = bookDao.getAllBooks();
        assertNotNull(books, "Book list should not be null");
        assertTrue(books.size() > 0, "There should be at least one book in the list");
    }

    /**
     * Tests deleting a book by its unique ID.
     */
    @Test
    @Order(5)
    void testDeleteBook() {
        bookDao.deleteBook(testBookId);

        // Verify the book was deleted
        Book book = bookDao.getBookById(testBookId);
        assertNull(book, "Book should be null after deletion");
    }

    /**
     * Cleans up the database after all tests are completed.
     */
    @AfterAll
    void cleanDatabase() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
