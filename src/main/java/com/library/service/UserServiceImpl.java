package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import com.library.service.exceptions.UserNotFoundException;
import com.library.service.exceptions.InvalidUserException;
import static com.library.util.ValidationUtil.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of the UserService interface.
 * Handles business logic for user-related operations.
 */
public class UserServiceImpl implements UserService {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); // Logger instance

    // UserDao instance
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

        // Log the user creation attempt
        logger.info("Attempting to create user: {}", user);

        // Validate user object
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }

        // Validate user fields 
        validateNotEmpty(user.getName(), "User name",
                () -> new InvalidUserException("User name cannot be null or empty."));
        validateEmail(user.getEmail(),
                () -> new InvalidUserException("Invalid email format."));

        // Check if the email is already in use
        User existingUser = userDao.getUserByEmail(user.getEmail());

        // If another user is found with the same email, throw an exception and log the
        // error
        if (existingUser != null) {

            logger.error("User creation failed: Email {} is already in use", user.getEmail());
            throw new InvalidUserException("Email is already in use.");
        }

        userDao.addUser(user); // Add the user
        logger.info("User created successfully with ID: {}", user.getId()); // Log the success
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User object if found
     */
    @Override
    public User getUserById(int id) {

        // Log the user retrieval attempt
        logger.info("Fetching user with ID: {}", id);

        // Validate user ID
        validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        // Fetch the user by ID
        User user = userDao.getUserById(id);

        // If the user is not found, throw an exception and log the warning
        if (user == null) {
            logger.warn("User with ID {} not found", id);
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        logger.info("User fetched successfully: {}", user); // Log the success
        return user; // Return the user if found
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all User objects
     */
    @Override
    public List<User> getAllUsers() {

        // Log the user retrieval attempt
        logger.info("Fetching all users");

        // Fetch all users
        List<User> users = userDao.getAllUsers();

        // Log the success and return the list
        logger.info("Successfully fetched {} users", users.size());
        return users;
    }

    /**
     * Updates an existing user's details after validation.
     *
     * @param user the User object with updated details
     */
    @Override
    public void updateUser(User user) {

        // Log the user update attempt
        logger.info("Attempting to update user: {}", user);

        // Validate user object
        if (user == null) {
            logger.error("User update failed: User object is null"); // Log the error
            throw new InvalidUserException("User cannot be null.");
        }

        // Validate user ID
        validatePositiveId(user.getId(), "User ID",
                () -> new InvalidUserException("Invalid user ID."));

        // Check if the user exists before updating
        User existingUser = userDao.getUserById(user.getId());

        // If the user is not found, throw an exception
        if (existingUser == null) {
            logger.warn("User update failed: User with ID {} not found", user.getId()); // Log the warning
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

                // If another user is found with the same email, throw an exception and log the
                // error
                if (userWithSameEmail != null) {
                    logger.error("User update failed: Email {} is already in use by another user", user.getEmail());
                    throw new InvalidUserException("Email is already in use by another user.");
                }
            }
        }

        userDao.updateUser(user); // Update the user
        logger.info("User updated successfully: {}", user); // Log the success
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique ID of the user to delete
     */
    @Override
    public void deleteUser(int id) {

        // Log the user deletion attempt
        logger.info("Attempting to delete user with ID: {}", id);

        // Validate user ID
        validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        // Verify user existence before deletion, log error and throw exception if not
        // found
        User user = userDao.getUserById(id);
        if (user == null) {
            logger.warn("User deletion failed: User with ID {} not found", id);
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        userDao.deleteUser(id); // Delete the user
        logger.info("User with ID {} deleted successfully", id); // Log the success
    }

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the email address of the user
     * @return the User object if found
     */
    @Override
    public User getUserByEmail(String email) {

        // Log the user retrieval attempt
        logger.info("Fetching user with email: {}", email);

        // Validate email format
        validateEmail(email,
                () -> new InvalidUserException("Invalid email format."));

        // Retrieve the user by email
        User user = userDao.getUserByEmail(email);

        // If the user is not found, throw an exception and log the warning
        if (user == null) {
            logger.warn("User with email {} not found", email);
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        logger.info("User fetched successfully: {}", user); // Log the success
        return user; // Return the user if found
    }
}