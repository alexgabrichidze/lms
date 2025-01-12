package com.library.service;

import com.library.dao.LoanDao;
import com.library.model.Loan;
import com.library.service.exceptions.InvalidLoanException;
import com.library.service.exceptions.LoanConflictException;
import com.library.service.exceptions.LoanNotFoundException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for LoanServiceImpl.
 * Tests CRUD operations and validations for LoanService.
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanDao loanDao; // Mock Loan DAO for testing

    private LoanServiceImpl loanService; // Service implementation to test

    private Loan mockLoan; // Mock loan for use in tests

    /**
     * Set up before each test. Resets mock interactions and initializes service and
     * mock data.
     */
    @BeforeEach
    void setUp() {
        reset(loanDao); // Reset mock interactions before each test
        loanService = new LoanServiceImpl(loanDao); // Initialize service with mock DAO
        mockLoan = new Loan(1, 1, 2, LocalDate.now(), null); // Mock loan
    }

    /**
     * Tests the creation of a valid loan.
     * Verifies that the loan is added successfully and the DAO methods are called
     * as expected.
     */
    @Test
    void testCreateValidLoan() {
        // Mock DAO behavior: no active loans for the book
        when(loanDao.getLoansByBookId(mockLoan.getBookId())).thenReturn(Collections.emptyList());

        // Call the service method to create the loan
        assertDoesNotThrow(() -> loanService.createLoan(mockLoan));

        // Verify that the DAO methods were called
        verify(loanDao, times(1)).addLoan(mockLoan);
        verify(loanDao, times(1)).getLoansByBookId(mockLoan.getBookId());
    }

    /**
     * Tests the creation of a loan with null input.
     * Verifies that an InvalidLoanException is thrown and no DAO interactions
     * occur.
     */
    @Test
    void testCreateNullLoan() {
        // Call the service method with null input
        assertThrows(InvalidLoanException.class, () -> loanService.createLoan(null));

        // Verify that no DAO interactions occurred
        verify(loanDao, never()).addLoan(any());
    }

    /**
     * Tests the creation of a loan when the book is already loaned.
     * Verifies that a LoanConflictException is thrown and no loan is added.
     */
    @Test
    void testCreateLoanAlreadyLoaned() {
        // Mock DAO behavior: book is already loaned
        when(loanDao.getLoansByBookId(mockLoan.getBookId())).thenReturn(List.of(mockLoan));

        // Call the service method and verify that it throws LoanConflictException
        assertThrows(LoanConflictException.class, () -> loanService.createLoan(mockLoan));

        // Verify that the DAO method was called but no loan was added
        verify(loanDao, times(1)).getLoansByBookId(mockLoan.getBookId());
        verify(loanDao, never()).addLoan(any());
    }

    /**
     * Tests the retrieval of a loan by its ID when the loan exists.
     * Verifies that the correct loan is returned and the DAO method is called.
     */
    @Test
    void testGetExistingLoanById() {
        // Mock DAO behavior: return the mock loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(mockLoan);

        // Call the service method to retrieve the loan
        Loan fetchedLoan = loanService.getLoanById(mockLoan.getId());

        // Assert that the returned loan matches the mock loan
        assertEquals(mockLoan, fetchedLoan);

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
    }

    /**
     * Tests the retrieval of a loan by its ID when the loan does not exist.
     * Verifies that a LoanNotFoundException is thrown.
     */
    @Test
    void testGetNonExistingLoanById() {
        // Mock DAO behavior: return null for non-existent loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(null);

        // Call the service method and verify that it throws LoanNotFoundException
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(mockLoan.getId()));

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
    }

    /**
     * Tests the retrieval of all loans.
     * Verifies that the correct list of loans is returned and the DAO method is
     * called.
     */
    @Test
    void getAllLoans() {
        // Mock DAO behavior: return a list containing the mock loan
        List<Loan> mockLoans = List.of(mockLoan);
        when(loanDao.getAllLoans()).thenReturn(mockLoans);

        // Call the service method to retrieve all loans
        List<Loan> fetchedLoans = loanService.getAllLoans();

        // Assert that the returned list matches the mock list
        assertEquals(mockLoans, fetchedLoans);

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getAllLoans();
    }

    /**
     * Tests the update of an existing loan.
     * Verifies that the loan is updated successfully and the DAO methods are
     * called.
     */
    @Test
    void testUpdateExistingLoan() {
        // Mock DAO behavior: return the existing loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(mockLoan);

        // Call the service method to update the loan
        assertDoesNotThrow(() -> loanService.updateLoan(mockLoan));

        // Verify that the DAO methods were called
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
        verify(loanDao, times(1)).updateLoan(mockLoan);
    }

    /**
     * Tests the update of a non-existent loan.
     * Verifies that a LoanNotFoundException is thrown and no update is performed.
     */
    @Test
    void testUpdateNonExistingLoan() {
        // Mock DAO behavior: return null for non-existent loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(null);

        // Call the service method and verify that it throws LoanNotFoundException
        assertThrows(LoanNotFoundException.class, () -> loanService.updateLoan(mockLoan));

        // Verify that the DAO method was called but no update was performed
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
        verify(loanDao, never()).updateLoan(any());
    }

    /**
     * Tests the deletion of an existing loan.
     * Verifies that the loan is deleted successfully and the DAO methods are
     * called.
     */
    @Test
    void testDeleteExistingLoan() {
        // Mock DAO behavior: return the existing loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(mockLoan);

        // Call the service method to delete the loan
        assertDoesNotThrow(() -> loanService.deleteLoan(mockLoan.getId()));

        // Verify that the DAO methods were called
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
        verify(loanDao, times(1)).deleteLoan(mockLoan.getId());
    }

    /**
     * Tests the deletion of a non-existent loan.
     * Verifies that a LoanNotFoundException is thrown and no deletion is performed.
     */
    @Test
    void testDeleteNonExistingLoan() {
        // Mock DAO behavior: return null for non-existent loan
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(null);

        // Call the service method and verify that it throws LoanNotFoundException
        assertThrows(LoanNotFoundException.class, () -> loanService.deleteLoan(mockLoan.getId()));

        // Verify that the DAO method was called but no deletion was performed
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
        verify(loanDao, never()).deleteLoan(mockLoan.getId()); // Use the specific ID instead of any()
    }

    /**
     * Tests the retrieval of loans by user ID.
     * Verifies that the correct list of loans is returned and the DAO method is
     * called.
     */
    @Test
    void testGetLoansByUserId() {
        // Mock DAO behavior: return a list containing the mock loan
        List<Loan> mockLoans = List.of(mockLoan);
        when(loanDao.getLoansByUserId(mockLoan.getUserId())).thenReturn(mockLoans);

        // Call the service method to retrieve loans by user ID
        List<Loan> fetchedLoans = loanService.getLoansByUserId(mockLoan.getUserId());

        // Assert that the returned list matches the mock list
        assertEquals(mockLoans, fetchedLoans);

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getLoansByUserId(mockLoan.getUserId());
    }

    /**
     * Tests the retrieval of loans by book ID.
     * Verifies that the correct list of loans is returned and the DAO method is
     * called.
     */
    @Test
    void testGetLoansByBookId() {
        // Mock DAO behavior: return a list containing the mock loan
        List<Loan> mockLoans = List.of(mockLoan);
        when(loanDao.getLoansByBookId(mockLoan.getBookId())).thenReturn(mockLoans);

        // Call the service method to retrieve loans by book ID
        List<Loan> fetchedLoans = loanService.getLoansByBookId(mockLoan.getBookId());

        // Assert that the returned list matches the mock list
        assertEquals(mockLoans, fetchedLoans);

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getLoansByBookId(mockLoan.getBookId());
    }

    /**
     * Tests the retrieval of active loans.
     * Verifies that the correct list of loans is returned and the DAO method is
     * called.
     */
    @Test
    void testGetActiveLoans() {
        // Mock DAO behavior: return a list containing the mock loan
        List<Loan> mockLoans = List.of(mockLoan);
        when(loanDao.getActiveLoans()).thenReturn(mockLoans);

        // Call the service method to retrieve active loans
        List<Loan> fetchedLoans = loanService.getActiveLoans();

        // Assert that the returned list matches the mock list
        assertEquals(mockLoans, fetchedLoans);

        // Verify that the DAO method was called
        verify(loanDao, times(1)).getActiveLoans();
    }
}