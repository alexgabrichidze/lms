package com.library.service;

import com.library.dao.LoanDao;
import com.library.dao.LoanDaoImpl;
import com.library.model.BookStatus;
import com.library.model.Loan;
import com.library.service.exceptions.InvalidLoanException;
import com.library.service.exceptions.LoanConflictException;
import com.library.service.exceptions.LoanNotFoundException;
import com.library.util.PaginatedResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
     * @param page the page number (zero-based)
     * @param size the number of loans per page
     * @return a list of all loans
     */
    @Override
    public PaginatedResponse<Loan> getAllLoans(int page, int size) {
        logger.info("Fetching all loans.");

        // Calculate offset
        int offset = page * size;

        // Fetch paginated loans
        List<Loan> loans = loanDao.getAllLoans(offset, size);
        long totalItems = loanDao.countLoans();

        // Check if nothing has been found
        if (loans.isEmpty()) {
            logger.warn("No loans found.");
            throw new LoanNotFoundException("No loans found.");
        }

        // Log success and return
        logger.info("Successfully fetched {} loans.", loans.size());
        return new PaginatedResponse<>(loans, page, size, totalItems);
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

        // Check if the loan exists before proceeding
        Loan existingLoan = loanDao.getLoanById(loan.getId());

        // If loan is not found, log a warning and throw an exception
        if (existingLoan == null) {
            logger.warn("Loan with ID {} not found for update.", loan.getId());
            throw new LoanNotFoundException("Loan with ID " + loan.getId() + " not found.");
        }

        // Merge updated fields into the existing loan
        if (loan.getUserId() != 0) {

            // Check if the user exists (by ID)
            if (userService.getUserById(loan.getUserId()) == null) {
                logger.warn("User with ID {} does not exist.", loan.getUserId());
                throw new InvalidLoanException("User with ID " + loan.getUserId() + " does not exist.");
            }
            existingLoan.setUserId(loan.getUserId());
        }

        if (loan.getBookId() != 0) {

            // Check if the book exists (by ID)
            if (bookService.getBookById(loan.getBookId()) == null) {
                logger.warn("Book with ID {} does not exist.", loan.getBookId());
                throw new InvalidLoanException("Book with ID " + loan.getBookId() + " does not exist.");
            }
            existingLoan.setBookId(loan.getBookId());
        }

        if (loan.getLoanDate() != null) {

            // Check if loan date is before return date
            if (loan.getLoanDate().isAfter(loan.getReturnDate())) {
                logger.warn("Loan date cannot be after return date.");
                throw new InvalidLoanException("Loan date cannot be after return date.");
            }
            existingLoan.setLoanDate(loan.getLoanDate());
        }

        if (loan.getReturnDate() != null) {

            // Check if return date is after loan date
            if (loan.getReturnDate().isBefore(loan.getLoanDate())) {
                logger.warn("Return date cannot be before loan date.");
                throw new InvalidLoanException("Return date cannot be before loan date.");
            }
            existingLoan.setReturnDate(loan.getReturnDate());
        }

        // Update the loan in the database and log the success
        loanDao.updateLoan(existingLoan);
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

        // Check if the loan exists
        Loan loan = loanDao.getLoanById(id);
        if (loan == null) {
            logger.warn("Loan with ID {} not found for deletion.", id);
            throw new LoanNotFoundException("Loan with ID " + id + " not found.");
        }

        // Delete the loan and log the success
        loanDao.deleteLoan(id);
        logger.info("Loan with ID {} deleted successfully.", id);
    }

    /**
     * Retrieves loans for a specific user using cursor-based pagination.
     *
     * @param userId the user ID for which to retrieve loans
     * @param cursor the last loan_date from the previous page (or null for first
     *               page)
     * @param limit  the number of loans per page
     * @return a paginated response containing loans and metadata
     */
    @Override
    public PaginatedResponse<Loan> getLoansByUserId(int userId, int limit, String cursor) {

        // Log the loan retrieval attempt
        logger.info("Fetching loans for user ID: {}", userId);

        // Fetch loans by user ID
        List<Loan> loans = loanDao.getLoansByUserId(userId, limit, cursor);
        long totalItems = loanDao.countLoansByUserId(userId);

        // Check if nothing has been found
        if (loans.isEmpty()) {
            logger.warn("No loans found.");
            throw new LoanNotFoundException("No loans found.");
        }

        // Generate next cursor from the last loan in the list
        Loan lastLoan = loans.get(loans.size() - 1);
        String nextCursor = lastLoan.getLoanDate() + "_" + lastLoan.getId();

        // Log the success and return the list
        logger.info("Successfully fetched {} loans for user ID: {}", loans.size(), userId);
        return new PaginatedResponse<>(loans, nextCursor, limit, totalItems);
    }
}
