package com.library.dao;

import com.library.model.Loan;
import java.util.List;

/**
 * Interface for managing loan-related database operations.
 * Provides methods to perform CRUD operations and paginated queries on the
 * loans
 * table.
 */
public interface LoanDao {

    /**
     * Adds a new loan to the database.
     *
     * @param loan the Loan object to be added
     */
    void addLoan(Loan loan);

    /**
     * Retrieves a loan from the database by its unique ID.
     *
     * @param id the ID of the loan to retrieve
     * @return the Loan object if found, or null if no loan with the given ID exists
     */
    Loan getLoanById(int id);

    /**
     * Retrieves paginated loans from the database.
     *
     * @param offset the number of loans to skip
     * @param limit  the maximum number of loans to retrieve
     * @return a list of Loan objects, or an empty list if no loans are found
     */
    List<Loan> getAllLoans(int offset, int limit);

    /**
     * Updates the details of an existing loan in the database.
     *
     * @param loan the Loan object containing the updated details
     */
    void updateLoan(Loan loan);

    /**
     * Deletes a loan from the database by its unique ID.
     *
     * @param id the ID of the loan to delete
     */
    void deleteLoan(int id);

    /**
     * Retrieves paginated loans for a specific user from database.
     *
     * @param userId the ID of the user
     * @param limit  the maximum number of loans to retrieve
     * @param cursor the cursor for the next page
     * @return a list of Loan objects for the user, or an empty list if none exist
     */
    List<Loan> getLoansByUserId(int userId, int limit, String cursor);

    /**
     * Counts the total number of loans in the database.
     *
     * @return the total number of loans
     */
    long countLoans();

    /**
     * Counts the total number of loans for a specific user.
     *
     * @param userId the ID of the user
     * @return the total number of loans for the user
     */
    long countLoansByUserId(int userId);
}
