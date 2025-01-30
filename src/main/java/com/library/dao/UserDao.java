package com.library.dao;

import com.library.model.User;

import java.util.List;

/**
 * Interface for managing user-related database operations.
 * Provides methods to perform CRUD operations and other queries on the users
 * table.
 */
public interface UserDao {

    /**
     * Adds a new user to the database.
     * 
     * @param user the User object containing the details of the user to be added
     */
    void addUser(User user);

    /**
     * Retrieves a user from the database by their unique ID.
     * 
     * @param id the ID of the user to retrieve
     * @return the User object if found, or null if no user with the given ID exists
     */
    User getUserById(int id);

    /**
     * Retrieves all users from the database.
     * 
     * @param offset the number of records to skip
     * @param limit  the maximum number of records to retrieve
     * @return a list of User objects, or an empty list if no users are found
     */
    List<User> getAllUsers(int offset, int limit);

    /**
     * Updates the details of an existing user in the database.
     * 
     * @param user the User object containing the updated details
     */
    void updateUser(User user);

    /**
     * Deletes a user from the database by their unique ID.
     * 
     * @param id the ID of the user to delete
     */
    void deleteUser(int id);

    /**
     * Retrieves a user from the database by their unique email address.
     * This method can be useful for login or authentication purposes.
     * 
     * @param email the email address of the user to retrieve
     * @return the User object if found, or null if no user with the given email
     *         exists
     */
    User getUserByEmail(String email);

    /**
     * Counts the total number of users in the database.
     *
     * @return the total number of users
     */
    long countAllUsers();
}
