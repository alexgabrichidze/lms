package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * Provides a centralized way to create and manage database connections.
 */
public class ConnectionManager {

    // Database connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/library"; // Replace with your database URL
    private static final String USER = System.getenv("DB_USERNAME"); // Retrieve the database username from environment
                                                                     // variables
    private static final String PASSWORD = System.getenv("DB_PASSWORD"); // Retrieve the database password from
                                                                         // environment variables

    /**
     * Private constructor to prevent instantiation.
     * This class provides static methods only and should not be instantiated.
     */
    private ConnectionManager() {
    }

    /**
     * Provides a connection to the database.
     * Uses the JDBC DriverManager to establish the connection.
     *
     * @return a Connection object for interacting with the database
     * @throws SQLException          if a database access error occurs
     * @throws IllegalStateException if environment variables for database
     *                               credentials are not set
     */
    public static Connection getConnection() throws SQLException {
        // Ensure that database credentials are set in environment variables
        if (USER == null || PASSWORD == null) {
            throw new IllegalStateException("Database credentials are not set in environment variables.");
        }

        // Create and return a new database connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
