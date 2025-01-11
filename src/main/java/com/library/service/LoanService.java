package com.library.service;

import com.library.model.Loan;

import java.util.List;

/**
 * Interface defining the contract for loan-related business operations.
 */
public interface LoanService {

    /**
     * Creates a new loan.
     *
     * @param loan the Loan object to be created
     */
    void createLoan(Loan loan);

    /**
     * Retrieves a loan by its unique ID.
     *
     * @param id the ID of the loan to retrieve
     * @return the Loan object if found
     */
    Loan getLoanById(int id);

    /**
     * Retrieves all loans in the system.
     *
     * @return a list of all Loan objects
     */
    List<Loan> getAllLoans();

    /**
     * Updates the details of an existing loan.
     *
     * @param loan the Loan object containing the updated details
     */
    void updateLoan(Loan loan);

    /**
     * Deletes a loan by its unique ID.
     *
     * @param id the ID of the loan to delete
     */
    void deleteLoan(int id);

    /**
     * Retrieves loans by user ID.
     *
     * @param userId the ID of the user whose loans are to be retrieved
     * @return a list of Loan objects for the specified user
     */
    List<Loan> getLoansByUserId(int userId);

    /**
     * Retrieves loans by book ID.
     *
     * @param bookId the ID of the book whose loans are to be retrieved
     * @return a list of Loan objects for the specified book
     */
    List<Loan> getLoansByBookId(int bookId);

    /**
     * Retrieves active loans (loans where the return date is null).
     *
     * @return a list of active Loan objects
     */
    List<Loan> getActiveLoans();
}
