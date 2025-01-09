package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import com.library.service.exceptions.UserNotFoundException;
import com.library.service.exceptions.InvalidUserException;
import static com.library.util.ValidationUtil.*;

import java.util.List;

/**
 * Implementation of the UserService interface.
 * Handles business logic for user-related operations.
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    /**
     * Constructor to initialize the UserDao implementation.
     */
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl(); // Default implementation
    }

    /**
     * Constructor to initialize UserService with custom UserDao class.
     *
     * @param userDao the custom UserDao implementation to use
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao; // Custom implementation
    }

    /**
     * Creates a new user after validating the input.
     *
     * @param user the User object to create
     */
    @Override
    public void createUser(User user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }

        // Validate fields using the utility methods
        validateNotEmpty(user.getName(), "User name",
                () -> new InvalidUserException("User name cannot be null or empty."));
        validateEmail(user.getEmail(),
                () -> new InvalidUserException("Invalid email format."));

        // Check if the email is already in use
        User existingUser = userDao.getUserByEmail(user.getEmail());

        // If another user is found with the same email, throw an exception
        if (existingUser != null) {
            throw new InvalidUserException("Email is already in use.");
        }

        userDao.addUser(user); // Add the user
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User object if found
     */
    @Override
    public User getUserById(int id) {

        // Validate user ID
        validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        // Fetch the user by ID
        User user = userDao.getUserById(id);

        // If the user is not found, throw an exception
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        return user; // Return the user if found
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all User objects
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers(); // Fetch all users
    }

    /**
     * Updates an existing user's details after validation.
     *
     * @param user the User object with updated details
     */
    @Override
    public void updateUser(User user) {

        // Validate user object
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }

        // Validate user ID
        validatePositiveId(user.getId(), "User ID",
                () -> new InvalidUserException("Invalid user ID."));

        // Check if the user exists before updating
        User existingUser = userDao.getUserById(user.getId());

        // If the user is not found, throw an exception
        if (existingUser == null) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found.");
        }

        // Validate name if updated
        if (user.getName() != null) {
            validateNotEmpty(user.getName(), "User name",
                    () -> new InvalidUserException("User name cannot be empty."));
        }

        // Validate email if updated
        if (user.getEmail() != null) {
            validateEmail(user.getEmail(),
                    () -> new InvalidUserException("Invalid email format."));

            // Check for email conflicts
            if (!user.getEmail().equals(existingUser.getEmail())) {

                // Check if the email is already in use
                User userWithSameEmail = userDao.getUserByEmail(user.getEmail());

                // If another user is found with the same email, throw an exception
                if (userWithSameEmail != null) {
                    throw new InvalidUserException("Email is already in use by another user.");
                }
            }
        }

        userDao.updateUser(user); // Update the user
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique ID of the user to delete
     */
    @Override
    public void deleteUser(int id) {

        // Validate user ID
        validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        // Check if the user exists before deletion
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        userDao.deleteUser(id); // Delete the user
    }

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the email address of the user
     * @return the User object if found
     */
    @Override
    public User getUserByEmail(String email) {

        // Validate email format
        validateEmail(email,
                () -> new InvalidUserException("Invalid email format."));

        // Fetch the user by email
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return user; // Return the user if found
    }
}