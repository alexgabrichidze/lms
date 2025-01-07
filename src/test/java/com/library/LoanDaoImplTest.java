package com.library;

import com.library.dao.LoanDao;
import com.library.dao.LoanDaoImpl;
import com.library.model.Loan;
import com.library.util.ConnectionManager;

import org.junit.jupiter.api.*;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoanDaoImpl.
 * Tests CRUD operations and specialized methods for the Loan DAO.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoanDaoImplTest {

    private LoanDao loanDao;
    @SuppressWarnings("unused")
    private int testLoanId;
    private int testUserId = 1; // Example test user ID
    private int testBookId = 1; // Example test book ID

    @BeforeAll
    void setUp() {
        loanDao = new LoanDaoImpl();

        // Add dummy user
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "INSERT INTO users (id, name, email, role) VALUES (1, 'Test User', 'testuser@example.com', 'USER') ON CONFLICT DO NOTHING");
            statement.executeUpdate(
                    "INSERT INTO books (id, title, author, isbn, published_date, status) VALUES (1, 'Test Book', 'Author', '1234567890123', '2023-01-01', 'AVAILABLE') ON CONFLICT DO NOTHING");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void cleanDatabase() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM loans");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void testAddLoan() {
        Loan loan = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        loanDao.addLoan(loan);
        assertTrue(loan.getId() > 0, "Loan ID should be set after insertion");
        testLoanId = loan.getId();
    }

    @Test
    @Order(2)
    void testGetLoanById() {
        Loan loan = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        loanDao.addLoan(loan);

        Loan fetchedLoan = loanDao.getLoanById(loan.getId());
        assertNotNull(fetchedLoan, "Loan should be found by ID");
        assertEquals(testUserId, fetchedLoan.getUserId());
        assertEquals(testBookId, fetchedLoan.getBookId());
        assertEquals(loan.getLoanDate(), fetchedLoan.getLoanDate());
        assertNull(fetchedLoan.getReturnDate(), "Return date should be null for an active loan");
    }

    @Test
    @Order(3)
    void testGetAllLoans() {
        Loan loan1 = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        Loan loan2 = new Loan(0, testUserId, testBookId, LocalDate.now().minusDays(1), LocalDate.now());
        loanDao.addLoan(loan1);
        loanDao.addLoan(loan2);

        List<Loan> loans = loanDao.getAllLoans();
        assertNotNull(loans, "Loans list should not be null");
        assertEquals(2, loans.size(), "There should be 2 loans in the list");
    }

    @Test
    @Order(4)
    void testUpdateLoan() {
        Loan loan = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        loanDao.addLoan(loan);

        // Update the return date
        loan.setReturnDate(LocalDate.now());
        loanDao.updateLoan(loan);

        Loan updatedLoan = loanDao.getLoanById(loan.getId());
        assertNotNull(updatedLoan, "Updated loan should be found");
        assertEquals(LocalDate.now(), updatedLoan.getReturnDate(), "Return date should match the updated value");
    }

    @Test
    @Order(5)
    void testDeleteLoan() {
        Loan loan = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        loanDao.addLoan(loan);

        loanDao.deleteLoan(loan.getId());
        Loan deletedLoan = loanDao.getLoanById(loan.getId());
        assertNull(deletedLoan, "Loan should be null after deletion");
    }

    @Test
    @Order(6)
    void testGetLoansByUserId() {
        Loan loan1 = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        Loan loan2 = new Loan(0, testUserId, testBookId, LocalDate.now().minusDays(1), null);
        loanDao.addLoan(loan1);
        loanDao.addLoan(loan2);

        List<Loan> userLoans = loanDao.getLoansByUserId(testUserId);
        assertNotNull(userLoans, "User loans list should not be null");
        assertEquals(2, userLoans.size(), "There should be 2 loans for the user");
    }

    @Test
    @Order(7)
    void testGetLoansByBookId() {
        Loan loan1 = new Loan(0, testUserId, testBookId, LocalDate.now(), null);
        loanDao.addLoan(loan1);

        List<Loan> bookLoans = loanDao.getLoansByBookId(testBookId);
        assertNotNull(bookLoans, "Book loans list should not be null");
        assertEquals(1, bookLoans.size(), "There should be 1 loan for the book");
    }

    @Test
    @Order(8)
    void testGetActiveLoans() {
        Loan loan1 = new Loan(0, testUserId, testBookId, LocalDate.now(), null); // Active loan
        Loan loan2 = new Loan(0, testUserId, testBookId, LocalDate.now().minusDays(1), LocalDate.now()); // Returned
                                                                                                         // loan
        loanDao.addLoan(loan1);
        loanDao.addLoan(loan2);

        List<Loan> activeLoans = loanDao.getActiveLoans();
        assertNotNull(activeLoans, "Active loans list should not be null");
        assertEquals(1, activeLoans.size(), "There should be 1 active loan");
        assertNull(activeLoans.get(0).getReturnDate(), "Return date of active loan should be null");
    }

    @AfterAll
    void cleanDatabaseAfterAll() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM loans");
            statement.executeUpdate("DELETE FROM users");
            statement.executeUpdate("DELETE FROM books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
