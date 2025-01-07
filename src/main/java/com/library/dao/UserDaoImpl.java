package com.library.dao;

import com.library.model.User;
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

            // Set the parameters for the insert query
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());

            // Execute the query and retrieve the generated ID
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

            statement.setInt(1, id); // Set the ID parameter for the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create a User object and set its fields
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
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
                // Add each user to the list
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("role")));
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

            // Set the parameters for the update query
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setInt(4, user.getId()); // Set the ID of the user to update

            // Execute the update query
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

            statement.setInt(1, id); // Set the ID parameter for the delete query
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

            statement.setString(1, email); // Set the email parameter for the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create a User object and set its fields
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }
}
