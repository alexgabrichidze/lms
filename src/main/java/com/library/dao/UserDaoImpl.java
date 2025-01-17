package com.library.dao;

import com.library.model.User;
import com.library.model.UserRole;
import com.library.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDao interface.
 * Provides methods to perform CRUD operations on the users table in the
 * database.
 */
public class UserDaoImpl implements UserDao {

    // Logger for debugging and error logging
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    /**
     * Adds a new user to the database.
     *
     * @param user the User object containing the details of the user to be added
     */
    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (name, email, role) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the user being added
            logger.info("Attempting to add user: {}", user);

            // Set the values of the prepared statement
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole().name()); // Convert enum to string

            // Execute the statement, retrieve and set the generated ID
            ResultSet resultSet = statement.executeQuery();

            // Set the generated ID in the User object
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id")); // Set the generated ID in the User object
                logger.info("User added successfully with ID: {}", user.getId()); // Log the user added
            }
        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while adding user: {}", user);
            throw new RuntimeException("Failed to add user", e);
        }
    }

    /**
     * Retrieves a user from the database by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object if found, or null if no user with the given ID exists
     */
    @Override
    public User getUserById(int id) {
        String sql = "SELECT id, name, email, role FROM users WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the user being fetched
            logger.info("Fetching user with ID: {}", id);

            // Set the value of the prepared statement
            statement.setInt(1, id);

            // Execute the statement and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Create and return a User object if a record is found
            if (resultSet.next()) {

                // Map the result set to a User object
                User user = mapResultSetToUser(resultSet);

                // Log the user fetched
                logger.info("User fetched successfully: {}", user);

                // Return the User object
                return user;
            } else {

                // Log a warning if no record is found
                logger.warn("No user found with ID: {}", id);

                // Return null if no record is found
                return null;
            }
        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while fetching user with ID: {}", id);
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of User objects, or an empty list if no users are found
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, role FROM users";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            // Log the users being fetched
            logger.info("Fetching all users.");

            // Create a User object for each record and add it to the list
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet)); // Map the result set to a User object
            }

            // Log the users fetched
            logger.info("Successfully fetched {} users.", users.size());

            // Return the list of User objects
            return users;

        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while fetching all users");
            throw new RuntimeException("Failed to fetch all users", e);
        }
    }

    /**
     * Updates the details of an existing user in the database.
     *
     * @param user the User object containing the updated details
     */
    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the user being updated
            logger.info("Updating user: {}", user);

            // Set the values of the prepared statement
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole().name()); // Convert enum to string
            statement.setInt(4, user.getId());

            // Execute the statement
            int rowsUpdated = statement.executeUpdate();

            // Log the number of rows updated
            logger.info("Updated {} row(s) for user ID: {}", rowsUpdated, user.getId());
        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while updating user: {}", user);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Deletes a user from the database by their unique ID.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the user being deleted
            logger.info("Attempting to delete user with ID: {}", id);

            // Set the value of the prepared statement
            statement.setInt(1, id);

            // Execute the statement
            int rowsDeleted = statement.executeUpdate();

            // Log the number of rows deleted
            logger.info("Deleted {} row(s) for user ID: {}", rowsDeleted, id);
        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while deleting user with ID: {}", id);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Retrieves a user from the database by their unique email address.
     *
     * @param email the email address of the user to retrieve
     * @return the User object if found, or null if no user with the given email
     *         exists
     */
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT id, name, email, role FROM users WHERE email = ?";
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Log the user being fetched
            logger.info("Fetching user with email: {}", email);

            // Set the value of the prepared statement
            statement.setString(1, email);

            // Execute the statement and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Create and return a User object if a record is found
            if (resultSet.next()) {

                // Map the result set to a User object
                User user = mapResultSetToUser(resultSet);

                // Log the user fetched
                logger.info("User fetched successfully: {}", user);

                return user; // Return the User object
            } else {

                // Log a warning if no record is found
                logger.warn("No user found with email: {}", email);

                return null; // Return null if no record is found
            }
        } catch (SQLException e) {

            // Log and throw a runtime exception if an error occurs
            logger.error("Error while fetching user with email: {}", email);
            throw new RuntimeException("Failed to fetch user by email", e);
        }
    }

    /**
     * Helper method to map a result set to a User object.
     *
     * @param resultSet the result set to map
     * @return the User object
     * @throws SQLException if a database access error occurs
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {

        // Create a new User object and set its attributes
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                UserRole.valueOf(resultSet.getString("role")) // Convert string to enum
        );

        // Log the User object mapped
        logger.debug("Mapped ResultSet to User: {}", user);
        return user; // Return the User object
    }
}
