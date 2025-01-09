package com.library.service;

import com.library.dao.UserDao;
import com.library.model.User;
import com.library.model.UserRole;
import com.library.service.exceptions.InvalidUserException;
import com.library.service.exceptions.UserNotFoundException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl.
 * Tests CRUD operations and validations for UserService.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao; // Mock User DAO for testing

    private UserServiceImpl userService; // Service implementation to test

    /**
     * Set up before each test. Resets mock interactions and initializes service.
     */
    @BeforeEach
    void setUp() {
        reset(userDao); // Reset mock interactions before each test
        userService = new UserServiceImpl(userDao); // Initialize the service with the mock DAO
    }

    /**
     * Tests the creation of a new user with valid inputs.
     * Ensures that the user is added successfully and DAO methods are called as
     * expected.
     */
    @Test
    void testCreateUser() {

        // Create a mock user
        User user = new User(0, "John Doe", "john.doe@example.com", UserRole.USER);

        // Define behavior for mock UserDao
        when(userDao.getUserByEmail("john.doe@example.com")).thenReturn(null);

        // Call the service method
        userService.createUser(user);

        // Verify that the DAO method to add a user was called once
        verify(userDao, times(1)).addUser(user);

        // Assert that the user role remains unchanged
        assertEquals(UserRole.USER, user.getRole());
    }

    /**
     * Tests the creation of a user with invalid inputs.
     * Verifies that appropriate exceptions are thrown for null or invalid data
     * and that no interactions are made with the DAO.
     */
    @Test
    void testCreateUserInvalidInput() {
        // Null user
        assertThrows(InvalidUserException.class, () -> userService.createUser(null),
                "Null user should throw exception");
        verifyNoInteractions(userDao); // Ensure no DAO interactions

        // Empty name
        assertThrows(InvalidUserException.class,
                () -> userService.createUser(new User(0, "", "invalid@example.com", UserRole.USER)),
                "Empty name should throw exception");
        verifyNoInteractions(userDao); // Ensure no DAO interactions

        // Empty email
        assertThrows(InvalidUserException.class,
                () -> userService.createUser(new User(0, "Name", "", UserRole.USER)),
                "Empty email should throw exception");
        verifyNoInteractions(userDao); // Ensure no DAO interactions
    }

    /**
     * Tests the retrieval of a user by their ID when the user exists.
     * Ensures that the correct user data is returned and the DAO method is called
     * as expected.
     */
    @Test
    void testGetUserById() {
        int userId = 1;

        // Create a mock user with the specified ID
        User mockUser = new User(userId, "John Doe", "john.doe@example.com",
                UserRole.USER);

        // Define behavior for mock UserDao: return a user with the specified ID
        when(userDao.getUserById(userId)).thenReturn(mockUser);

        // Call the service method
        User user = userService.getUserById(userId);

        // Assert the returned user's properties
        assertNotNull(user, "User should be found by ID");
        assertEquals("John Doe", user.getName(), "Name should match");
        assertEquals("john.doe@example.com", user.getEmail(), "Email should match");
        assertEquals(UserRole.USER, user.getRole(), "Role should match");

        // Verify the DAO method was called exactly once
        verify(userDao, times(1)).getUserById(userId);
    }

    /**
     * Tests the retrieval of a user by their ID when the user does not exist.
     * Verifies that a UserNotFoundException is thrown and the correct DAO method is
     * called.
     */
    @Test
    void testGetUserByIdNotFound() {
        int nonExistentUserId = 9999;

        // Define behavior for mock UserDao: return null for non-existent user
        when(userDao.getUserById(nonExistentUserId)).thenReturn(null);

        // Call the service method and assert exception
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(nonExistentUserId),
                "Non-existent ID should throw exception");

        // Verify exception message
        assertEquals("User with ID 9999 not found.", exception.getMessage(),
                "Exception message should match");

        // Verify the DAO method was called exactly once
        verify(userDao, times(1)).getUserById(nonExistentUserId);
    }

    /**
     * Tests the retrieval of all users from the database.
     * Verifies that the correct list of users is returned and the DAO method is
     * called.
     */
    @Test
    void testGetAllUsers() {
        // Mock a list of users
        List<User> mockUsers = List.of(
                new User(1, "John Doe", "john.doe@example.com", UserRole.USER),
                new User(2, "Jane Smith", "jane.smith@example.com", UserRole.ADMIN));

        // Define behavior for mock UserDao
        when(userDao.getAllUsers()).thenReturn(mockUsers);

        // Call the service method
        List<User> users = userService.getAllUsers();

        // Assert the returned list
        assertNotNull(users, "User list should not be null");
        assertEquals(2, users.size(), "User list size should match the mocked data");

        // Verify the properties of the first user
        assertEquals("John Doe", users.get(0).getName(), "First user's name should match");
        assertEquals("john.doe@example.com", users.get(0).getEmail(), "First user's email should match");
        assertEquals(UserRole.USER, users.get(0).getRole(), "First user's role should match");

        // Verify the properties of the second user
        assertEquals("Jane Smith", users.get(1).getName(), "Second user's name should match");
        assertEquals("jane.smith@example.com", users.get(1).getEmail(), "Second user's email should match");
        assertEquals(UserRole.ADMIN, users.get(1).getRole(), "Second user's role should match");

        // Verify the DAO method was called once
        verify(userDao, times(1)).getAllUsers();
    }

    /**
     * Tests the update of an existing user's details with valid data.
     * Verifies that the DAO methods for retrieving and updating the user are called
     * as expected.
     */
    @Test
    void testUpdateUser() {
        // Define the user ID and mock data
        int userId = 1;
        User existingUser = new User(userId, "John Doe", "john.doe@example.com", UserRole.USER);
        User updatedUser = new User(userId, "Jane Doe", "jane.doe@example.com", UserRole.ADMIN);

        // Define behavior for mock UserDao
        when(userDao.getUserById(userId)).thenReturn(existingUser);

        // Call the service method with updated user details
        userService.updateUser(updatedUser);

        // Verify that the DAO methods were called with the correct arguments
        verify(userDao, times(1)).getUserById(userId);
        verify(userDao, times(1)).updateUser(updatedUser);

        // Assert that the mock existingUser remains unaffected
        assertEquals("John Doe", existingUser.getName(), "Existing user's name should remain unchanged");
        assertEquals("john.doe@example.com", existingUser.getEmail(), "Existing user's email should remain unchanged");
        assertEquals(UserRole.USER, existingUser.getRole(), "Existing user's role should remain unchanged");
    }

    /**
     * Tests the update of a non-existent user.
     * Ensures that a UserNotFoundException is thrown and no update is attempted in
     * the DAO.
     */
    @Test
    void testUpdateUserInvalid() {
        // Define a non-existent user
        int nonExistentUserId = 9999;
        User nonExistentUser = new User(nonExistentUserId, "Non-existent", "nonexistent@example.com", UserRole.USER);

        // Define behavior for mock UserDao
        when(userDao.getUserById(nonExistentUserId)).thenReturn(null);

        // Attempt to update the non-existent user
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonExistentUser),
                "Updating a non-existent user should throw UserNotFoundException");

        // Verify that the DAO's getUserById method was called but not updateUser
        verify(userDao, times(1)).getUserById(nonExistentUserId);
        verify(userDao, never()).updateUser(nonExistentUser);
    }

    /**
     * Tests the deletion of a user by their ID when the user exists.
     * Verifies that the DAO methods for retrieving and deleting the user are called
     * as expected.
     */
    @Test
    void testDeleteUser() {
        // Define the ID and mock user
        int userId = 1;
        User mockUser = new User(userId, "John Doe", "john.doe@example.com", UserRole.USER);

        // Define behavior for mock UserDao
        when(userDao.getUserById(userId)).thenReturn(mockUser);

        // Call the service method
        userService.deleteUser(userId);

        // Verify the DAO's getUserById and deleteUser methods were called
        verify(userDao, times(1)).getUserById(userId);
        verify(userDao, times(1)).deleteUser(userId);
    }

    /**
     * Tests the retrieval of a user by their email address when the user exists.
     * Ensures that the correct user data is returned and the DAO method is called.
     */
    @Test
    void testGetUserByEmail() {
        // Define email and mock user
        String email = "test.email@example.com";
        User mockUser = new User(1, "Test User", email, UserRole.USER);

        // Define behavior for mock UserDao
        when(userDao.getUserByEmail(email)).thenReturn(mockUser);

        // Call the service method
        User user = userService.getUserByEmail(email);

        // Assert that the returned user matches the expected data
        assertNotNull(user, "User should not be null");
        assertEquals("Test User", user.getName(), "Name should match");
        assertEquals(email, user.getEmail(), "Email should match");
        assertEquals(UserRole.USER, user.getRole(), "Role should match");

        // Verify that the DAO method was called once
        verify(userDao, times(1)).getUserByEmail(email);
    }

    /**
     * Tests the retrieval of a user by their email address when the user does not
     * exist.
     * Verifies that a UserNotFoundException is thrown and the correct DAO method is
     * called.
     */
    @Test
    void testGetUserByEmailNotFound() {
        // Define email for a user that doesn't exist
        String nonExistentEmail = "nonexistent@example.com";

        // Define behavior for mock UserDao
        when(userDao.getUserByEmail(nonExistentEmail)).thenReturn(null);

        // Call the service method and assert that it throws UserNotFoundException
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserByEmail(nonExistentEmail),
                "Non-existent email should throw UserNotFoundException");

        // Verify that the DAO method was called once
        verify(userDao, times(1)).getUserByEmail(nonExistentEmail);
    }

}