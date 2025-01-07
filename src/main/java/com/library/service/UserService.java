package com.library.service;

import com.library.model.User;
import java.util.List;
import com.library.service.exceptions.InvalidUserException;
import com.library.service.exceptions.UserNotFoundException;

/**
 * Interface for user-related business logic.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the User object to create
     * @throws InvalidUserException if the user data is invalid
     */
    void createUser(User user) throws InvalidUserException;

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User object if found
     * @throws UserNotFoundException if no user with the given ID exists
     */
    User getUserById(int id) throws UserNotFoundException;

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all User objects
     */
    List<User> getAllUsers();

    /**
     * Updates an existing user's details.
     *
     * @param user the User object with updated details
     * @throws InvalidUserException  if the updated user data is invalid
     * @throws UserNotFoundException if no user with the given ID exists
     */
    void updateUser(User user) throws InvalidUserException, UserNotFoundException;

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique ID of the user to delete
     * @throws UserNotFoundException if no user with the given ID exists
     */
    void deleteUser(int id) throws UserNotFoundException;

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the email address of the user
     * @return the User object if found
     * @throws UserNotFoundException if no user with the given email exists
     */
    User getUserByEmail(String email) throws UserNotFoundException;
}