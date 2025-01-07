package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import com.library.service.exceptions.UserNotFoundException;
import com.library.service.exceptions.InvalidUserException;
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
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidUserException("User name cannot be null or empty.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserException("User email cannot be null or empty.");
        }

        // Check if the email is already in use
        User existingUser = userDao.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new InvalidUserException("Email is already in use.");
        }

        // If validation passes, create the user
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
        if (user == null || user.getId() <= 0) {
            throw new InvalidUserException("Invalid user ID.");
        }

        User existingUser = userDao.getUserById(user.getId());
        if (existingUser == null) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found.");
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            User userWithSameEmail = userDao.getUserByEmail(user.getEmail());
            if (userWithSameEmail != null) {
                throw new InvalidUserException("Email is already in use by another user.");
            }
        }
        // Update the user
        userDao.updateUser(user);
    }

    /**
     * Deletes a user by their unique ID.
     * 
     * @param id the unique ID of the user to delete
     */
    @Override
    public void deleteUser(int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        // Additional checks (e.g., active loans) could be added here
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
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return user;
    }
}
