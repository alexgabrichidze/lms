package com.library.dao;

import com.library.model.Loan;
import java.util.List;

/**
 * Interface for managing loan-related database operations.
 * Provides methods to perform CRUD operations and other queries on the loans
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
     * Retrieves all loans from the database.
     *
     * @return a list of Loan objects, or an empty list if no loans are found
     */
    List<Loan> getAllLoans();

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
     * Retrieves all loans for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of Loan objects for the user, or an empty list if none exist
     */
    List<Loan> getLoansByUserId(int userId);

    /**
     * Retrieves all loans for a specific book.
     *
     * @param bookId the ID of the book
     * @return a list of Loan objects for the book, or an empty list if none exist
     */
    List<Loan> getLoansByBookId(int bookId);
}
