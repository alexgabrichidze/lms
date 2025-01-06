package com.library;

import com.library.dao.*;
import com.library.model.Book;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDaoImplTest {
    private BookDao bookDao;
    private int testBookId;

    @BeforeAll
    void setUp() {
        bookDao = new BookDaoImpl(); // Initialize the DAO
    }

    @Test
    @Order(1)
    void testAddBook() {
        Book book = new Book("1984", "George Orwell", "1234567890123", LocalDate.of(1949, 6, 8), "AVAILABLE");
        bookDao.addBook(book);

        // Verify the book was added by fetching all books
        List<Book> books = bookDao.getAllBooks();
        assertTrue(books.size() > 0, "At least one book should exist after adding");
        testBookId = books.get(books.size() - 1).getId(); // Capture the ID of the last added book
    }

    @Test
    @Order(2)
    void testGetBookById() {
        Book book = bookDao.getBookById(testBookId);
        assertNotNull(book, "Book should be found by ID");
        assertEquals("1984", book.getTitle(), "Title should match");
        assertEquals("George Orwell", book.getAuthor(), "Author should match");
    }

    @Test
    @Order(3)
    void testUpdateBook() {
        Book book = bookDao.getBookById(testBookId);
        assertNotNull(book, "Book should exist for updating");

        // Update the book details
        book.setTitle("Animal Farm");
        book.setAuthor("George Orwell");
        book.setStatus("BORROWED");
        bookDao.updateBook(book);

        // Fetch the updated book and verify the changes
        Book updatedBook = bookDao.getBookById(testBookId);
        assertEquals("Animal Farm", updatedBook.getTitle(), "Updated title should match");
        assertEquals("BORROWED", updatedBook.getStatus(), "Updated status should match");
    }

    @Test
    @Order(4)
    void testGetAllBooks() {
        List<Book> books = bookDao.getAllBooks();
        assertNotNull(books, "Book list should not be null");
        assertTrue(books.size() > 0, "There should be at least one book in the list");
    }

    @Test
    @Order(5)
    void testDeleteBook() {
        bookDao.deleteBook(testBookId);

        // Verify the book was deleted
        Book book = bookDao.getBookById(testBookId);
        assertNull(book, "Book should be null after deletion");
    }
}