package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import com.library.service.exceptions.UserNotFoundException;
import com.library.service.exceptions.InvalidUserException;
import com.library.util.ValidationUtil;

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
        this.userDao = new UserDaoImpl();
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
        ValidationUtil.validateNotEmpty(user.getName(), "User name",
                () -> new InvalidUserException("User name cannot be null or empty."));
        ValidationUtil.validateEmail(user.getEmail(),
                () -> new InvalidUserException("Invalid email format."));

        // Check if the email is already in use
        User existingUser = userDao.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new InvalidUserException("Email is already in use.");
        }

        userDao.addUser(user);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User object if found
     */
    @Override
    public User getUserById(int id) {
        ValidationUtil.validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        User user = userDao.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        return user;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all User objects
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Updates an existing user's details after validation.
     *
     * @param user the User object with updated details
     */
    @Override
    public void updateUser(User user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }
        ValidationUtil.validatePositiveId(user.getId(), "User ID",
                () -> new InvalidUserException("Invalid user ID."));

        User existingUser = userDao.getUserById(user.getId());
        if (existingUser == null) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found.");
        }

        // Validate name if updated
        if (user.getName() != null) {
            ValidationUtil.validateNotEmpty(user.getName(), "User name",
                    () -> new InvalidUserException("User name cannot be empty."));
        }

        // Validate email if updated
        if (user.getEmail() != null) {
            ValidationUtil.validateEmail(user.getEmail(),
                    () -> new InvalidUserException("Invalid email format."));

            // Check for email conflicts
            if (!user.getEmail().equals(existingUser.getEmail())) {
                User userWithSameEmail = userDao.getUserByEmail(user.getEmail());
                if (userWithSameEmail != null) {
                    throw new InvalidUserException("Email is already in use by another user.");
                }
            }
        }

        userDao.updateUser(user);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique ID of the user to delete
     */
    @Override
    public void deleteUser(int id) {
        ValidationUtil.validatePositiveId(id, "User ID",
                () -> new InvalidUserException("User ID must be a positive integer."));

        User user = userDao.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        userDao.deleteUser(id);
    }

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the email address of the user
     * @return the User object if found
     */
    @Override
    public User getUserByEmail(String email) {
        ValidationUtil.validateEmail(email,
                () -> new InvalidUserException("Invalid email format."));

        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return user;
    }
}