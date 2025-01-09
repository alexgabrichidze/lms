package com.library.service;

import com.library.dao.BookDao;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.service.exceptions.InvalidBookException;
import com.library.service.exceptions.BookNotFoundException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * Test class for BookServiceImpl.
 * Tests CRUD operations and validations for BookService.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookDao bookDao; // Mock Book DAO for testing

    private BookServiceImpl bookService; // Service implementation to test

    private Book mockBook; // Mock book for use in tests

    /**
     * Set up before each test. Resets mock interactions and initializes service and
     * mock data.
     */
    @BeforeEach
    void setUp() {
        reset(bookDao); // Reset mock interactions before each test
        bookService = new BookServiceImpl(bookDao); // Initialize service with mock DAO
        mockBook = new Book(1, "1984", "George Orwell", "1234567890123", null, BookStatus.AVAILABLE); // Mock book
    }

    /**
     * Tests the addition of a valid book to the library.
     * Verifies that the DAO method is called and the default status is set to
     * AVAILABLE.
     */
    @Test
    void testAddBook() {

        // Call the service method to add the book
        bookService.addBook(mockBook);

        // Verify that the DAO method was called once
        verify(bookDao, times(1)).addBook(mockBook);

        // Assert that the book's default status is AVAILABLE
        assertEquals(BookStatus.AVAILABLE, mockBook.getStatus(), "Default status should be AVAILABLE");
    }

    /**
     * Tests the addition of a book with invalid inputs.
     * Verifies that appropriate exceptions are thrown for null or invalid data.
     */
    @Test
    void testAddBookInvalidInput() {

        // Verify that adding a null book throws InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.addBook(null), "Null book should throw exception");

        // Create an invalid book with an empty title
        Book invalidBook = new Book(0, "", "Author", "1234567890123", null, BookStatus.AVAILABLE);

        // Verify that adding a book with an empty title throws InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.addBook(invalidBook),
                "Empty title should throw exception");
    }

    /**
     * Tests the retrieval of a book by its ID when the book exists.
     * Verifies that the correct book is returned and the DAO method is called.
     */
    @Test
    void testGetBookById() {

        // Define behavior for the mock DAO to return the mock book
        when(bookDao.getBookById(1)).thenReturn(mockBook);

        // Call the service method to get the book by ID
        Book book = bookService.getBookById(1);

        // Assert that the book is not null
        assertNotNull(book, "Book should not be null");
        assertEquals("1984", book.getTitle(), "Book title should match"); // Assert that the book title matches the mock
                                                                          // data

        // Verify that the DAO method was called exactly once
        verify(bookDao, times(1)).getBookById(1);
    }

    /**
     * Tests the retrieval of a book by its ID when the book does not exist.
     * Verifies that a BookNotFoundException is thrown.
     */
    @Test
    void testGetBookByIdNotFound() {
        // Define behavior for the mock DAO to return null for non-existent ID
        when(bookDao.getBookById(1)).thenReturn(null);

        // Call the service method and verify that it throws BookNotFoundException
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1),
                "Non-existent ID should throw exception");
    }

    /**
     * Tests the retrieval of all books from the library.
     * Verifies that the correct list of books is returned and the DAO method is
     * called.
     */
    @Test
    void testGetAllBooks() {

        // Define behavior for the mock DAO to return a list with the mock book
        when(bookDao.getAllBooks()).thenReturn(Arrays.asList(mockBook));

        // Call the service method to get all books
        List<Book> books = bookService.getAllBooks();

        // Assert that the list is not null and not empty
        assertNotNull(books, "Books list should not be null");
        assertFalse(books.isEmpty(), "Books list should not be empty"); // Assert that the list is not empty

        // Verify that the DAO method was called exactly once
        verify(bookDao, times(1)).getAllBooks();
    }

    /**
     * Tests the update of a book's details when the book exists.
     * Verifies that the DAO method for updating is called.
     */
    @Test
    void testUpdateBook() {

        // Define behavior for the mock DAO to return the existing book
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        // Update the book title
        mockBook.setTitle("Animal Farm");

        // Call the service method to update the book
        bookService.updateBook(mockBook);

        // Verify that the DAO method for updating was called once
        verify(bookDao, times(1)).updateBook(mockBook);
    }

    /**
     * Tests the update of a book's details when the book does not exist.
     * Verifies that a BookNotFoundException is thrown.
     */
    @Test
    void testUpdateBookNotFound() {

        // Define behavior for the mock DAO to return null for non-existent ID
        when(bookDao.getBookById(mockBook.getId())).thenReturn(null);

        // Call the service method and verify that it throws BookNotFoundException
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(mockBook),
                "Non-existent ID should throw exception");
    }

    /**
     * Tests the deletion of a book by its ID when the book exists.
     * Verifies that the DAO methods for retrieval and deletion are called.
     */
    @Test
    void testDeleteBook() {

        // Define behavior for the mock DAO to return the mock book
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        // Call the service method to delete the book
        bookService.deleteBook(mockBook.getId());

        // Verify that the DAO method for deletion was called once
        verify(bookDao, times(1)).deleteBook(mockBook.getId());
    }

    /**
     * Tests the deletion of a book by its ID when the book does not exist.
     * Verifies that a BookNotFoundException is thrown.
     */
    @Test
    void testDeleteBookNotFound() {

        // Define behavior for the mock DAO to return null for non-existent ID
        when(bookDao.getBookById(1)).thenReturn(null);

        // Call the service method and verify that it throws BookNotFoundException
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1),
                "Non-existent ID should throw exception");
    }

    /**
     * Tests the retrieval of books by their title.
     * Verifies that the correct list of books is returned and the DAO method is
     * called.
     */
    @Test
    void testGetBooksByTitle() {

        // Mock DAO behavior to return a list containing the mock book for the given
        // title
        when(bookDao.getBooksByTitle("1984")).thenReturn(Arrays.asList(mockBook));

        // Call the service method to retrieve books by title
        List<Book> books = bookService.getBooksByTitle("1984");

        // Assert that the list of books is not null
        assertNotNull(books, "Books list should not be null");

        // Assert that the list is not empty
        assertFalse(books.isEmpty(), "Books list should not be empty");

        // Assert that the list contains the expected number of books
        assertEquals(1, books.size(), "Books list size should match");

        // Verify that the DAO's getBooksByTitle method was called exactly once with the
        // correct argument
        verify(bookDao, times(1)).getBooksByTitle("1984");
    }

    /**
     * Tests the retrieval of books by their title with invalid input.
     * Verifies that an InvalidBookException is thrown for an empty title.
     */
    @Test
    void testGetBooksByTitleInvalid() {

        // Verify that calling the service method with an empty title throws
        // InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.getBooksByTitle(""),
                "Empty title should throw exception");
    }

    /**
     * Tests the retrieval of books by their author.
     * Verifies that the correct list of books is returned and the DAO method is
     * called.
     */
    @Test
    void testGetBooksByAuthor() {

        // Mock DAO behavior to return a list containing the mock book for the given
        // author
        when(bookDao.getBooksByAuthor("George Orwell")).thenReturn(Arrays.asList(mockBook));

        // Call the service method to retrieve books by author
        List<Book> books = bookService.getBooksByAuthor("George Orwell");

        // Assert that the list of books is not null
        assertNotNull(books, "Books list should not be null");

        // Assert that the list is not empty
        assertFalse(books.isEmpty(), "Books list should not be empty");

        // Assert that the list contains the expected number of books
        assertEquals(1, books.size(), "Books list size should match");

        // Verify that the DAO's getBooksByAuthor method was called exactly once with
        // the correct argument
        verify(bookDao, times(1)).getBooksByAuthor("George Orwell");
    }

    /**
     * Tests the retrieval of books by their author with invalid input.
     * Verifies that an InvalidBookException is thrown for an empty author.
     */
    @Test
    void testGetBooksByAuthorInvalid() {

        // Verify that calling the service method with an empty author throws
        // InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.getBooksByAuthor(""),
                "Empty author should throw exception");
    }

    /**
     * Tests the retrieval of a book by its ISBN.
     * Verifies that the correct book is returned and the DAO method is called.
     */
    @Test
    void testGetBookByIsbn() {

        // Mock DAO behavior to return the mock book for the given ISBN
        when(bookDao.getBookByIsbn("1234567890123")).thenReturn(mockBook);

        // Call the service method to retrieve the book by ISBN
        Book book = bookService.getBookByIsbn("1234567890123");

        // Assert that the retrieved book is not null
        assertNotNull(book, "Book should not be null");

        // Assert that the book title matches the expected value
        assertEquals("1984", book.getTitle(), "Book title should match");

        // Verify that the DAO's getBookByIsbn method was called exactly once with the
        // correct argument
        verify(bookDao, times(1)).getBookByIsbn("1234567890123");
    }

    /**
     * Tests the retrieval of a book by its ISBN with invalid input.
     * Verifies that an InvalidBookException is thrown for an empty ISBN.
     */
    @Test
    void testGetBookByIsbnInvalid() {

        // Verify that calling the service method with an empty ISBN throws
        // InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.getBookByIsbn(""),
                "Empty ISBN should throw exception");
    }

    /**
     * Tests the update of a book's status.
     * Verifies that the status is updated correctly and the DAO method is called.
     */
    @Test
    void testUpdateBookStatus() {

        // Mock DAO behavior to return the mock book for the given ID
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        // Call the service method to update the book's status
        bookService.updateBookStatus(mockBook.getId(), BookStatus.BORROWED);

        // Verify that the DAO's updateBook method was called exactly once
        verify(bookDao, times(1)).updateBook(mockBook);

        // Assert that the book's status was updated to the new value
        assertEquals(BookStatus.BORROWED, mockBook.getStatus(), "Status should be updated to BORROWED");
    }

    /**
     * Tests the update of a book's status with invalid input.
     * Verifies that appropriate exceptions are thrown for invalid IDs or statuses.
     */
    @Test
    void testUpdateBookStatusInvalid() {

        // Verify that calling the service method with an invalid ID throws
        // InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.updateBookStatus(0, BookStatus.BORROWED),
                "Invalid ID should throw exception");

        // Verify that calling the service method with a null status throws
        // InvalidBookException
        assertThrows(InvalidBookException.class, () -> bookService.updateBookStatus(1, null),
                "Null status should throw exception");
    }

    /**
     * Tests the update of a book's status when the book does not exist.
     * Verifies that a BookNotFoundException is thrown.
     */
    @Test
    void testUpdateBookStatusNotFound() {

        // Mock DAO behavior to return null for non-existent book ID
        when(bookDao.getBookById(1)).thenReturn(null);

        // Call the service method and verify that it throws BookNotFoundException
        assertThrows(BookNotFoundException.class, () -> bookService.updateBookStatus(1, BookStatus.BORROWED),
                "Non-existent ID should throw exception");
    }
}
