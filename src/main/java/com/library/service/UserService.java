package com.library.service;

import com.library.model.User;
import com.library.util.PaginatedResponse;

/**
 * Interface for user-related business logic.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the User object to create
     */
    void createUser(User user);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User object if found
     */
    User getUserById(int id);

    /**
     * Retrieves a paginated list of users.
     *
     * @param page the page number (zero-based)
     * @param size the number of users per page
     * @return a paginated response containing users and metadata
     */
    PaginatedResponse<User> getAllUsers(int page, int size);

    /**
     * Updates an existing user's details.
     *
     * @param user the User object with updated details
     */
    void updateUser(User user);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique ID of the user to delete
     */
    void deleteUser(int id);

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the email address of the user
     * @return the User object if found
     */
    User getUserByEmail(String email);
}