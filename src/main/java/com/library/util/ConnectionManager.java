package com.library.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * Provides a centralized way to create and manage database connections.
 */
public class ConnectionManager {

    // Load environment variables from the .env file
    private static final Dotenv dotenv = Dotenv.load();

    // Database connection details
    private static final String URL = dotenv.get("DB_URL"); // Retrieve the database URL from the .env file
    private static final String USER = dotenv.get("DB_USERNAME"); // Retrieve the database username
    private static final String PASSWORD = dotenv.get("DB_PASSWORD"); // Retrieve the database password

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
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        // Ensure that database credentials are set
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Database credentials are not properly set in the .env file.");
        }

        // Create and return a new database connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
