package com.library.dao;

import com.library.model.User;
import com.library.model.UserRole;
import com.library.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDao interface.
 * Provides methods to perform CRUD operations on the users table in the
 * database.
 */
public class UserDaoImpl implements UserDao {

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

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole().name()); // Convert enum to string

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id")); // Set the generated ID in the User object
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole().name()); // Convert enum to string
            statement.setInt(4, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to map a result set to a User object.
     *
     * @param resultSet the result set to map
     * @return the User object
     * @throws SQLException if a database access error occurs
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                UserRole.valueOf(resultSet.getString("role").toUpperCase())// Convert string to enum
        );
        return user;
    }
}
