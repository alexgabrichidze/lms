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

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookDao bookDao;

    private BookServiceImpl bookService;

    private Book mockBook;

    @BeforeEach
    void setUp() {
        reset(bookDao); // Reset mock interactions before each test
        bookService = new BookServiceImpl(bookDao);
        mockBook = new Book(1, "1984", "George Orwell", "1234567890123", null, BookStatus.AVAILABLE);
    }

    @Test
    void testAddBook() {
        bookService.addBook(mockBook);

        verify(bookDao, times(1)).addBook(mockBook);
        assertEquals(BookStatus.AVAILABLE, mockBook.getStatus(), "Default status should be AVAILABLE");
    }

    @Test
    void testAddBookInvalidInput() {
        assertThrows(InvalidBookException.class, () -> bookService.addBook(null), "Null book should throw exception");

        Book invalidBook = new Book(0, "", "Author", "1234567890123", null, BookStatus.AVAILABLE);
        assertThrows(InvalidBookException.class, () -> bookService.addBook(invalidBook),
                "Empty title should throw exception");
    }

    @Test
    void testGetBookById() {
        when(bookDao.getBookById(1)).thenReturn(mockBook);

        Book book = bookService.getBookById(1);

        assertNotNull(book, "Book should not be null");
        assertEquals("1984", book.getTitle(), "Book title should match");
        verify(bookDao, times(1)).getBookById(1);
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookDao.getBookById(1)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1),
                "Non-existent ID should throw exception");
    }

    @Test
    void testGetAllBooks() {
        when(bookDao.getAllBooks()).thenReturn(Arrays.asList(mockBook));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books, "Books list should not be null");
        assertFalse(books.isEmpty(), "Books list should not be empty");
        verify(bookDao, times(1)).getAllBooks();
    }

    @Test
    void testUpdateBook() {
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        mockBook.setTitle("Animal Farm");
        bookService.updateBook(mockBook);

        verify(bookDao, times(1)).updateBook(mockBook);
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookDao.getBookById(mockBook.getId())).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(mockBook),
                "Non-existent ID should throw exception");
    }

    @Test
    void testDeleteBook() {
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        bookService.deleteBook(mockBook.getId());

        verify(bookDao, times(1)).deleteBook(mockBook.getId());
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookDao.getBookById(1)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1),
                "Non-existent ID should throw exception");
    }

    @Test
    void testGetBooksByTitle() {
        when(bookDao.getBooksByTitle("1984")).thenReturn(Arrays.asList(mockBook));

        List<Book> books = bookService.getBooksByTitle("1984");

        assertNotNull(books, "Books list should not be null");
        assertFalse(books.isEmpty(), "Books list should not be empty");
        assertEquals(1, books.size(), "Books list size should match");
        verify(bookDao, times(1)).getBooksByTitle("1984");
    }

    @Test
    void testGetBooksByTitleInvalid() {
        assertThrows(InvalidBookException.class, () -> bookService.getBooksByTitle(""),
                "Empty title should throw exception");
    }

    @Test
    void testGetBooksByAuthor() {
        when(bookDao.getBooksByAuthor("George Orwell")).thenReturn(Arrays.asList(mockBook));

        List<Book> books = bookService.getBooksByAuthor("George Orwell");

        assertNotNull(books, "Books list should not be null");
        assertFalse(books.isEmpty(), "Books list should not be empty");
        assertEquals(1, books.size(), "Books list size should match");
        verify(bookDao, times(1)).getBooksByAuthor("George Orwell");
    }

    @Test
    void testGetBooksByAuthorInvalid() {
        assertThrows(InvalidBookException.class, () -> bookService.getBooksByAuthor(""),
                "Empty author should throw exception");
    }

    @Test
    void testGetBookByIsbn() {
        when(bookDao.getBookByIsbn("1234567890123")).thenReturn(mockBook);

        Book book = bookService.getBookByIsbn("1234567890123");

        assertNotNull(book, "Book should not be null");
        assertEquals("1984", book.getTitle(), "Book title should match");
        verify(bookDao, times(1)).getBookByIsbn("1234567890123");
    }

    @Test
    void testGetBookByIsbnInvalid() {
        assertThrows(InvalidBookException.class, () -> bookService.getBookByIsbn(""),
                "Empty ISBN should throw exception");
    }

    @Test
    void testUpdateBookStatus() {
        when(bookDao.getBookById(mockBook.getId())).thenReturn(mockBook);

        bookService.updateBookStatus(mockBook.getId(), BookStatus.BORROWED);

        verify(bookDao, times(1)).updateBook(mockBook);
        assertEquals(BookStatus.BORROWED, mockBook.getStatus(), "Status should be updated to BORROWED");
    }

    @Test
    void testUpdateBookStatusInvalid() {
        assertThrows(InvalidBookException.class, () -> bookService.updateBookStatus(0, BookStatus.BORROWED),
                "Invalid ID should throw exception");
        assertThrows(InvalidBookException.class, () -> bookService.updateBookStatus(1, null),
                "Null status should throw exception");
    }

    @Test
    void testUpdateBookStatusNotFound() {
        when(bookDao.getBookById(1)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBookStatus(1, BookStatus.BORROWED),
                "Non-existent ID should throw exception");
    }
}
