package com.library.dao;

import com.library.model.Loan;
import com.library.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the LoanDao interface.
 * Provides methods to perform CRUD operations and specific queries on the loans
 * table.
 */
public class LoanDaoImpl implements LoanDao {

    private static final Logger logger = LoggerFactory.getLogger(LoanDaoImpl.class);

    @Override
    public void addLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?) RETURNING id";

        logger.debug("Attempting to add loan: {}", loan); // Log the loan details before execution

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters of the insert statement
            statement.setInt(1, loan.getUserId());
            statement.setInt(2, loan.getBookId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);

            // Execute the insert statement and get the generated ID
            ResultSet resultSet = statement.executeQuery();

            // Check if a loan is added and log the generated ID
            if (resultSet.next()) {
                loan.setId(resultSet.getInt("id"));
                logger.info("Loan added successfully with ID: {}", loan.getId()); // Log success with generated ID
            }
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while adding loan: {}", loan, e); // Log the error with details
            throw new RuntimeException("Failed to add loan", e);
        }
    }

    @Override
    public Loan getLoanById(int id) {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans WHERE id = ?";
        logger.debug("Fetching loan with ID: {}", id); // Log the ID being fetched

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID parameter and execute the query
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Check if a loan is found and return it, otherwise return null
            if (resultSet.next()) {
                Loan loan = mapResultSetToLoan(resultSet);
                logger.info("Loan fetched successfully: {}", loan); // Log the fetched loan
                return loan;
            } else {
                logger.warn("No loan found with ID: {}", id); // Log if no loan is found
                return null;
            }
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while fetching loan with ID: {}", id, e); // Log the error
            throw new RuntimeException("Failed to fetch loan", e);
        }
    }

    @Override
    public List<Loan> getAllLoans() {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans";
        logger.debug("Fetching all loans"); // Log the fetch action

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                // Map each row to a Loan object and add it to the list
                Loan loan = mapResultSetToLoan(resultSet);
                loans.add(loan);
            }

            // Log the number of loans fetched and return the list of loans
            logger.info("Successfully fetched {} loans", loans.size()); // Log the number of loans fetched
            return loans;
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while fetching all loans", e); // Log the error
            throw new RuntimeException("Failed to fetch all loans", e);
        }
    }

    @Override
    public void updateLoan(Loan loan) {
        String sql = "UPDATE loans SET user_id = ?, book_id = ?, loan_date = ?, return_date = ? WHERE id = ?";

        logger.debug("Updating loan: {}", loan); // Log the loan being updated

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters of the update statement
            statement.setInt(1, loan.getUserId());
            statement.setInt(2, loan.getBookId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            statement.setInt(5, loan.getId());

            // Execute the update statement and log the number of rows updated
            int rowsUpdated = statement.executeUpdate();
            logger.info("Updated {} row(s) for loan ID: {}", rowsUpdated, loan.getId()); // Log the update result
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while updating loan: {}", loan, e); // Log the error
            throw new RuntimeException("Failed to update loan", e);
        }
    }

    @Override
    public void deleteLoan(int id) {
        String sql = "DELETE FROM loans WHERE id = ?";

        logger.debug("Deleting loan with ID: {}", id); // Log the ID being deleted

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            // Execute the delete statement and log the number of rows deleted
            int rowsDeleted = statement.executeUpdate();
            logger.info("Deleted {} row(s) for loan ID: {}", rowsDeleted, id);
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while deleting loan with ID: {}", id, e);
            throw new RuntimeException("Failed to delete loan", e);
        }
    }

    @Override
    public List<Loan> getLoansByUserId(int userId) {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans WHERE user_id = ?";
        logger.debug("Fetching loans for user ID: {}", userId); // Log the user ID

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each loan to the list
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }

            // Log the number of loans fetched and return the list of loans
            logger.info("Fetched {} loans for user ID: {}", loans.size(), userId); // Log the number of loans fetched
            return loans;
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while fetching loans for user ID: {}", userId, e); // Log the error
            throw new RuntimeException("Failed to fetch loans", e);
        }
    }

    @Override
    public List<Loan> getLoansByBookId(int bookId) {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans WHERE book_id = ?";
        logger.debug("Fetching loans for book ID: {}", bookId); // Log the book ID

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate over the result set and add each loan to the list
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }

            // Log the number of loans fetched and return the list of loans
            logger.info("Fetched {} loans for book ID: {}", loans.size(), bookId); // Log the number of loans fetched
            return loans;
        } catch (SQLException e) {
            logger.error("Error while fetching loans for book ID: {}", bookId, e); // Log the error
            throw new RuntimeException("Failed to fetch loans", e);
        }
    }

    @Override
    public List<Loan> getActiveLoans() {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans WHERE return_date IS NULL";
        logger.debug("Fetching active loans"); // Log the fetch action

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate over the result set and add each loan to the list
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }

            // Log the number of active loans fetched and return the list of loans
            logger.info("Fetched {} active loans", loans.size());
            return loans;
        } catch (SQLException e) {

            // Log the error and throw a new runtime exception
            logger.error("Error while fetching active loans", e);
            throw new RuntimeException("Failed to fetch active loans", e);
        }
    }

    /**
     * Helper method to map a result set to a Loan object.
     *
     * @param resultSet the result set to map
     * @return the Loan object
     * @throws SQLException if a database access error occurs
     */
    private Loan mapResultSetToLoan(ResultSet resultSet) throws SQLException {

        // Create a new User object and set its attributes
        Loan loan = new Loan(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("book_id"),
                resultSet.getDate("loan_date").toLocalDate(),
                resultSet.getDate("return_date").toLocalDate()

        );

        // Log the Loan object mapped
        logger.debug("Mapped ResultSet to Loan: {}", loan);
        return loan; // Return the Loan object
    }
}
