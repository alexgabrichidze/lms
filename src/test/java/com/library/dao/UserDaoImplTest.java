package com.library.dao;

import com.library.model.User;
import com.library.model.UserRole;
import com.library.util.ConnectionManager;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserDaoImpl.
 * Tests CRUD operations and other methods for the User DAO.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoImplTest {

    private UserDao userDao;
    private int testUserId;

    @BeforeAll
    void setUp() {
        userDao = new UserDaoImpl(); // Initialize the UserDao implementation
    }

    @Test
    @Order(1)
    void testAddUser() {
        User user = new User(0, "John Doe", "john.doe@example.com", UserRole.USER);
        userDao.addUser(user);
        assertTrue(user.getId() > 0, "User ID should be set after insertion");
        testUserId = user.getId();
    }

    @Test
    @Order(2)
    void testGetUserById() {
        User user = userDao.getUserById(testUserId);
        assertNotNull(user, "User should be found by ID");
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    @Order(3)
    void testGetAllUsers() {
        List<User> users = userDao.getAllUsers();
        assertNotNull(users, "User list should not be null");
        assertFalse(users.isEmpty(), "There should be at least one user in the list");
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        User user = userDao.getUserById(testUserId);
        assertNotNull(user, "User should exist before update");

        // Update user details
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setRole(UserRole.ADMIN);
        userDao.updateUser(user);

        // Verify the update
        User updatedUser = userDao.getUserById(testUserId);
        assertEquals("Jane Doe", updatedUser.getName(), "Updated name should match");
        assertEquals("jane.doe@example.com", updatedUser.getEmail(), "Updated email should match");
        assertEquals(UserRole.ADMIN, updatedUser.getRole(), "Updated role should match");
    }

    @Test
    @Order(5)
    void testDeleteUser() {
        userDao.deleteUser(testUserId);

        // Verify the deletion
        User user = userDao.getUserById(testUserId);
        assertNull(user, "User should be null after deletion");
    }

    @Test
    @Order(6)
    void testGetUserByEmail() {
        // Add a new user
        User user = new User(0, "Email Tester", "test.email@example.com", UserRole.USER);
        userDao.addUser(user);

        // Fetch by email
        User fetchedUser = userDao.getUserByEmail("test.email@example.com");
        assertNotNull(fetchedUser, "User should be found by email");
        assertEquals("Email Tester", fetchedUser.getName());
        assertEquals(UserRole.USER, fetchedUser.getRole());
    }

    @AfterAll
    void cleanDatabase() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
