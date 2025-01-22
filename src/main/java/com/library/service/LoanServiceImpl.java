package com.library.service;

import com.library.dao.LoanDao;
import com.library.dao.LoanDaoImpl;
import com.library.model.BookStatus;
import com.library.model.Loan;
import com.library.service.exceptions.InvalidLoanException;
import com.library.service.exceptions.LoanConflictException;
import com.library.service.exceptions.LoanNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.library.util.ValidationUtil.*;

/**
 * Implementation of the LoanService interface.
 * Handles business logic for loan-related operations, including validation.
 */
public class LoanServiceImpl implements LoanService {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    // Data access object for loans
    private final LoanDao loanDao;

    // Book service object for books
    private final BookService bookService;

    // User service object for users
    private final UserService userService;

    /**
     * Default constructor initializing with the default LoanDao implementation.
     */
    public LoanServiceImpl() {

        // Default implementation of LoanDao
        this.loanDao = new LoanDaoImpl();

        // Default implementation of BookService
        this.bookService = new BookServiceImpl();

        // Default implementation of UserService
        this.userService = new UserServiceImpl();
    }

    /**
     * Constructor for providing a custom LoanDao implementation.
     *
     * @param loanDao the custom LoanDao implementation to use
     */
    public LoanServiceImpl(LoanDao loanDao, BookService bookService, UserService userService) {

        // Custom implementation of LoanDao
        this.loanDao = loanDao;

        // Default implementation of BookService
        this.bookService = new BookServiceImpl();

        // Default implementation of UserService
        this.userService = new UserServiceImpl();
    }

    /**
     * Creates a new loan.
     *
     * @param loan the loan to be created
     */
    @Override
    public void createLoan(Loan loan) {

        // Log the loan creation attempt
        logger.info("Attempting to create loan: {}", loan);

        // Check if the book exists (by ID)
        if (bookService.getBookById(loan.getBookId()) == null) {
            throw new InvalidLoanException("Book with ID " + loan.getBookId() + " does not exist.");
        }

        // Check if the user exists (by ID)
        if (userService.getUserById(loan.getUserId()) == null) {
            throw new InvalidLoanException("User with ID " + loan.getUserId() + " does not exist.");
        }

        // Check if the book is available
        if (!bookService.getBookById(loan.getBookId()).getStatus().equals(BookStatus.AVAILABLE)) {
            throw new LoanConflictException("Book with ID " + loan.getBookId() + " is not available.");
        }

        // Change book's status to BORROWED
        bookService.updateBookStatus(loan.getBookId(), BookStatus.BORROWED);

        // Add loan to the loans table and log the success
        loanDao.addLoan(loan);
        logger.info("Loan created successfully: {}", loan);
    }

    /**
     * Retrieves a loan by its unique ID.
     *
     * @param id the unique ID of the loan
     * @return the loan object if found
     */
    @Override
    public Loan getLoanById(int id) {

        // Log the loan retrieval attempt
        logger.info("Fetching loan with ID: {}", id);

        // Fetch the loan by ID and check if the loan exists
        Loan loan = loanDao.getLoanById(id);
        if (loan == null) {
            throw new LoanNotFoundException("Loan with ID " + id + " not found.");
        }

        // Log the success
        logger.info("Loan fetched successfully: {}", loan);
        return loan;
    }

    /**
     * Retrieves all loans in the system.
     *
     * @return a list of all loans
     */
    @Override
    public List<Loan> getAllLoans() {

        // Log the loan retrieval attempt
        logger.info("Fetching all loans.");

        // Fetch all loans
        List<Loan> loans = loanDao.getAllLoans();

        // Check if nothing has been found
        if (loans.isEmpty()) {
            logger.warn("No loans found.");
            throw new LoanNotFoundException("No loans found.");
        }

        // Log the success and return the list
        logger.info("Successfully fetched {} loans.", loans.size());
        return loans;
    }

    /**
     * Updates an existing loan.
     *
     * @param loan the loan with updated details
     */
    @Override
    public void updateLoan(Loan loan) {

        // Log the loan update attempt
        logger.info("Attempting to update loan: {}", loan);

        // Validate loan object
        if (loan == null) {
            throw new InvalidLoanException("Loan cannot be null.");
        }

        // Validate loan ID
        validatePositiveId(loan.getId(), "Loan ID",
                () -> new InvalidLoanException("Loan ID must be a positive integer."));

        // Check if the loan exists
        Loan existingLoan = loanDao.getLoanById(loan.getId());
        if (existingLoan == null) {
            throw new LoanNotFoundException("Loan with ID " + loan.getId() + " not found.");
        }

        // Validate user ID and book ID if updated
        validatePositiveId(loan.getUserId(), "User ID",
                () -> new InvalidLoanException("User ID must be a positive integer."));
        validatePositiveId(loan.getBookId(), "Book ID",
                () -> new InvalidLoanException("Book ID must be a positive integer."));

        // Update the loan in the database and log the success
        loanDao.updateLoan(loan);
        logger.info("Loan updated successfully: {}", loan);
    }

    /**
     * Deletes a loan by its unique ID.
     *
     * @param id the unique ID of the loan
     */
    @Override
    public void deleteLoan(int id) {

        // Log the loan deletion attempt
        logger.info("Attempting to delete loan with ID: {}", id);

        // Validate loan ID
        validatePositiveId(id, "Loan ID",
                () -> new InvalidLoanException("Loan ID must be a positive integer."));

        // Check if the loan exists
        Loan loan = loanDao.getLoanById(id);
        if (loan == null) {
            throw new LoanNotFoundException("Loan with ID " + id + " not found.");
        }

        // Delete the loan and log the success
        loanDao.deleteLoan(id);
        logger.info("Loan with ID {} deleted successfully.", id);
    }

    /**
     * Retrieves loans for a specific book by its book ID.
     *
     * @param bookId the book ID for which to retrieve loans
     * @return a list of loans associated with the book
     */
    @Override
    public List<Loan> getLoansByUserId(int userId) {

        // Log the loan retrieval attempt
        logger.info("Fetching loans for user ID: {}", userId);

        // Validate user ID
        validatePositiveId(userId, "User ID",
                () -> new InvalidLoanException("User ID must be a positive integer."));

        // Fetch loans by user ID
        List<Loan> loans = loanDao.getLoansByUserId(userId);

        // Log the success and return the list
        logger.info("Successfully fetched {} loans for user ID: {}", loans.size(), userId);
        return loans;
    }

    /**
     * Retrieves loans for a specific book by its book ID.
     *
     * @param bookId the book ID for which to retrieve loans
     * @return a list of loans associated with the book
     */
    @Override
    public List<Loan> getLoansByBookId(int bookId) {

        // Log the loan retrieval attempt
        logger.info("Fetching loans for book ID: {}", bookId);

        // Validate book ID
        validatePositiveId(bookId, "Book ID",
                () -> new InvalidLoanException("Book ID must be a positive integer."));

        // Fetch loans by book ID
        List<Loan> loans = loanDao.getLoansByBookId(bookId);

        // Log the success and return the list
        logger.info("Successfully fetched {} loans for book ID: {}", loans.size(), bookId);
        return loans;
    }

    /**
     * Retrieves all active loans (loans that have not been returned).
     *
     * @return a list of active loans
     */
    @Override
    public List<Loan> getActiveLoans() {

        // Log the loan retrieval attempt
        logger.info("Fetching active loans.");

        // Fetch active loans
        List<Loan> loans = loanDao.getActiveLoans();

        // Log the success and return the list
        logger.info("Successfully fetched {} active loans.", loans.size());
        return loans;
    }
}
