package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import com.library.model.UserRole;
import com.library.service.exceptions.InvalidUserException;
import com.library.service.exceptions.UserNotFoundException;
import com.library.util.ConnectionManager;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl.
 * Tests CRUD operations and validations for UserService.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTest {
    private UserServiceImpl userService;

    @SuppressWarnings("unused")
    private UserDao userDao;
    private int testUserId;

    @BeforeAll
    void setUp() {
        userDao = new UserDaoImpl(); // Use the actual DAO
        userService = new UserServiceImpl(); // Use the actual service
    }

    @Test
    @Order(1)
    void testCreateUser() {
        User user = new User(0, "John Doe", "john.doe@example.com", UserRole.USER);
        userService.createUser(user);
        assertTrue(user.getId() > 0, "User ID should be set after insertion");
        testUserId = user.getId();
    }

    @Test
    @Order(2)
    void testCreateUserInvalidInput() {
        assertThrows(InvalidUserException.class, () -> userService.createUser(null),
                "Null user should throw exception");
        assertThrows(InvalidUserException.class,
                () -> userService.createUser(new User(0, "", "invalid@example.com", UserRole.USER)),
                "Empty name should throw exception");
        assertThrows(InvalidUserException.class, () -> userService.createUser(new User(0, "Name", "", UserRole.USER)),
                "Empty email should throw exception");
    }

    @Test
    @Order(3)
    void testGetUserById() {
        User user = userService.getUserById(testUserId);
        assertNotNull(user, "User should be found by ID");
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    @Order(4)
    void testGetUserByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(9999),
                "Non-existent ID should throw exception");
    }

    @Test
    @Order(5)
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users, "User list should not be null");
        assertFalse(users.isEmpty(), "There should be at least one user in the list");
    }

    @Test
    @Order(6)
    void testUpdateUser() {
        User user = userService.getUserById(testUserId);
        assertNotNull(user, "User should exist before update");

        // Update user details
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setRole(UserRole.ADMIN);
        userService.updateUser(user);

        // Verify the update
        User updatedUser = userService.getUserById(testUserId);
        assertEquals("Jane Doe", updatedUser.getName(), "Updated name should match");
        assertEquals("jane.doe@example.com", updatedUser.getEmail(), "Updated email should match");
        assertEquals(UserRole.ADMIN, updatedUser.getRole(), "Updated role should match");
    }

    @Test
    @Order(7)
    void testUpdateUserInvalid() {
        User invalidUser = new User(9999, "Non-existent", "nonexistent@example.com", UserRole.USER);
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(invalidUser),
                "Updating non-existent user should throw exception");
    }

    @Test
    @Order(8)
    void testDeleteUser() {
        userService.deleteUser(testUserId);

        // Verify the deletion
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(testUserId),
                "Deleted user should not exist");
    }

    @Test
    @Order(9)
    void testGetUserByEmail() {
        // Add a new user
        User user = new User(0, "Email Tester", "test.email@example.com", UserRole.USER);
        userService.createUser(user);

        // Fetch by email
        User fetchedUser = userService.getUserByEmail("test.email@example.com");
        assertNotNull(fetchedUser, "User should be found by email");
        assertEquals("Email Tester", fetchedUser.getName());
        assertEquals(UserRole.USER, fetchedUser.getRole());
    }

    @Test
    @Order(10)
    void testGetUserByEmailNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"),
                "Non-existent email should throw exception");
    }

    @AfterAll
    void cleanDatabase() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM loans");
            statement.executeUpdate("DELETE FROM users");
            statement.executeUpdate("DELETE FROM books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}