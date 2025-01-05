package com.library.model;

import java.time.LocalDate;

/**
 * Represents a loan in the library management system.
 * Includes attributes such as user ID, book ID, loan date, and return date.
 */
public class Loan {
    private int id; // Unique identifier for the loan (auto-generated by the database)
    private int userId; // ID of the user who borrowed the book
    private int bookId; // ID of the borrowed book
    private LocalDate loanDate; // Date when the book was borrowed
    private LocalDate returnDate; // Date when the book was returned (nullable)

    /**
     * Default constructor for creating an empty Loan object.
     */
    public Loan() {
    }

    /**
     * Parameterized constructor for creating a Loan with specified attributes.
     *
     * @param userId     the ID of the user who borrowed the book (must be positive)
     * @param bookId     the ID of the borrowed book (must be positive)
     * @param loanDate   the loan date (defaults to current date if null)
     * @param returnDate the return date (nullable, must not be before loan date)
     */
    public Loan(int userId, int bookId, LocalDate loanDate, LocalDate returnDate) {
        this.setUserId(userId); // Validates and sets the user ID
        this.setBookId(bookId); // Validates and sets the book ID
        this.setLoanDate(loanDate); // Validates and sets the loan date
        this.setReturnDate(returnDate); // Validates and sets the return date
    }

    /**
     * Gets the unique identifier of the loan.
     *
     * @return the loan ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier of the loan.
     *
     * @param id the loan ID (must be non-negative)
     * @throws IllegalArgumentException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
        this.id = id;
    }

    /**
     * Gets the user ID associated with the loan.
     *
     * @return the user ID
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Sets the user ID associated with the loan.
     *
     * @param userId the user ID (must be positive)
     * @throws IllegalArgumentException if the user ID is not positive
     */
    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }
        this.userId = userId;
    }

    /**
     * Gets the book ID associated with the loan.
     *
     * @return the book ID
     */
    public int getBookId() {
        return this.bookId;
    }

    /**
     * Sets the book ID associated with the loan.
     *
     * @param bookId the book ID (must be positive)
     * @throws IllegalArgumentException if the book ID is not positive
     */
    public void setBookId(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive.");
        }
        this.bookId = bookId;
    }

    /**
     * Gets the loan date.
     *
     * @return the loan date
     */
    public LocalDate getLoanDate() {
        return this.loanDate;
    }

    /**
     * Sets the loan date.
     *
     * @param loanDate the loan date (defaults to current date if null)
     */
    public void setLoanDate(LocalDate loanDate) {
        if (loanDate == null) {
            this.loanDate = LocalDate.now(); // Default to current date
        } else {
            this.loanDate = loanDate;
        }
    }

    /**
     * Gets the return date of the loan.
     *
     * @return the return date
     */
    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    /**
     * Sets the return date of the loan.
     *
     * @param returnDate the return date (nullable, must not be before loan date)
     * @throws IllegalArgumentException if return date is before loan date
     */
    public void setReturnDate(LocalDate returnDate) {
        if (returnDate != null && returnDate.isBefore(this.loanDate)) {
            throw new IllegalArgumentException("Return date cannot be before the loan date.");
        }
        this.returnDate = returnDate;
    }

    /**
     * Provides a string representation of the loan.
     *
     * @return a string representation of the loan
     */
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
