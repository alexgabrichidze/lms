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
 * Unit tests for LoanServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    // TODO: Дописать тесты + комментарии

    @Mock
    private LoanDao loanDao;

    private LoanServiceImpl loanService;

    private Loan mockLoan;

    @BeforeEach
    void setUp() {
        reset(loanDao); // Reset mock LoanDao
        loanService = new LoanServiceImpl(loanDao); // Inject mock LoanDao
        mockLoan = new Loan(1, 1, 2, LocalDate.now(), null); // Mock loan
    }

    @Test
    void testCreateValidLoan() {
        when(loanDao.getLoansByBookId(mockLoan.getBookId())).thenReturn(Collections.emptyList());
        doNothing().when(loanDao).addLoan(mockLoan);

        assertDoesNotThrow(() -> loanService.createLoan(mockLoan));

        verify(loanDao, times(1)).addLoan(mockLoan);
        verify(loanDao, times(1)).getLoansByBookId(mockLoan.getBookId());
    }

    @Test
    void testCreateNullLoan() {
        assertThrows(InvalidLoanException.class, () -> loanService.createLoan(null));
    }

    @Test
    void testCreateLoanAlreadyLoaned() {
        when(loanDao.getLoansByBookId(mockLoan.getBookId())).thenReturn(List.of(mockLoan));

        assertThrows(LoanConflictException.class, () -> loanService.createLoan(mockLoan));

        verify(loanDao, times(1)).getLoansByBookId(mockLoan.getBookId());
    }

    @Test
    void testGetExistingLoanById() {
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(mockLoan);

        Loan fetchedLoan = loanService.getLoanById(mockLoan.getId());

        assertEquals(mockLoan, fetchedLoan);
        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
    }

    @Test
    void testGetNonExistingLoanById() {
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(null);

        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(mockLoan.getId()));

        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
    }

    @Test
    void getAllLoans() {
        List<Loan> mockLoans = List.of(mockLoan);
        when(loanDao.getAllLoans()).thenReturn(mockLoans);

        List<Loan> fetchedLoans = loanService.getAllLoans();

        assertEquals(mockLoans, fetchedLoans);
        verify(loanDao, times(1)).getAllLoans();
    }

    @Test
    void testUpdateExistingLoan() {
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(mockLoan);

        doNothing().when(loanDao).updateLoan(mockLoan);

        assertDoesNotThrow(() -> loanService.updateLoan(mockLoan));

        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
        verify(loanDao, times(1)).updateLoan(mockLoan);
    }

    @Test
    void testUpdateNonExistingLoan() {
        when(loanDao.getLoanById(mockLoan.getId())).thenReturn(null);

        assertThrows(LoanNotFoundException.class, () -> loanService.updateLoan(mockLoan));

        verify(loanDao, times(1)).getLoanById(mockLoan.getId());
    }
}
