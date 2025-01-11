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

}
