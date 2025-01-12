package com.library.service;

import com.library.model.Loan;


import java.util.List;

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
     * @return a list of all loans
     */
    List<Loan> getAllLoans();

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
     * @return a list of loans associated with the user
     */
    List<Loan> getLoansByUserId(int userId);

    /**
     * Retrieves loans for a specific book by its book ID.
     *
     * @param bookId the book ID for which to retrieve loans
     * @return a list of loans associated with the book
     */
    List<Loan> getLoansByBookId(int bookId);

    /**
     * Retrieves all active loans (loans that have not been returned).
     *
     * @return a list of active loans
     */
    List<Loan> getActiveLoans();
}
