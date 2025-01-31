package com.library.service;

import com.library.model.Loan;
import com.library.util.PaginatedResponse;

/**
 * Service interface for managing loans in the library system.
 */
public interface LoanService {
    /**
     * Creates a new loan.
     *
     * @param loan the loan to be created
     */
    void createLoan(Loan loan);

    /**
     * Retrieves a loan by its unique ID.
     *
     * @param id the unique ID of the loan
     * @return the loan object if found
     */
    Loan getLoanById(int id);

    /**
     * Retrieves all loans in the system.
     *
     * @param page the page number (zero-based)
     * @param size the number of loans per page
     * @return a list of all loans
     */
    PaginatedResponse<Loan> getAllLoans(int page, int size);

    /**
     * Updates an existing loan.
     *
     * @param loan the loan with updated details
     */
    void updateLoan(Loan loan);

    /**
     * Deletes a loan by its unique ID.
     *
     * @param id the unique ID of the loan
     */
    void deleteLoan(int id);

    /**
     * Retrieves loans for a specific user by their user ID.
     *
     * @param userId the user ID for which to retrieve loans
     * @param page   the page number (zero-based)
     * @param size   the number of loans per page
     * @return a list of loans associated with the user
     */
    PaginatedResponse<Loan> getLoansByUserId(int userId, int page, int size);
}
