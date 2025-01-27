package com.library.controller;

import com.library.service.UserService;
import com.library.service.exceptions.InvalidUserException;
import com.library.service.exceptions.UserNotFoundException;
import java.util.List;
import java.util.Map;
import com.library.model.User;
import static com.library.util.ValidationUtil.*;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.slf4j.Logger;

/**
 * Controller for handling user-related HTTP requests.
 * Provides endpoints for managing users in the library, including CRUD
 * operations and search functionality.
 */
public class UserController extends BaseController {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // UserService object
    private final UserService userService;

    /**
     * Constructs a new UserController object with a custom UserService object.
     *
     * @param userService The UserService object to be used by the UserController.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles incoming HTTP requests and routes them to the appropriate method
     * based on the request path and method.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Extract request method, path and query
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            // Log the incoming request
            logger.info("Received {} request for path: {}", method, path);

            // Route the request based on the path
            if (path.matches("/users")) {
                if (query != null) {
                    handleSearchUsers(exchange, query, method);
                } else {
                    handleUsersEndpoint(exchange, method);
                }
            } else if (path.matches("/users/\\d+")) {
                int id = extractIdFromPath(path);
                validatePositiveId(id, "User ID", () -> new InvalidUserException("Invalid user ID"));
                handleUserEndpoint(exchange, method, id);
            } else {
                // Handle path not found errors
                sendResponse(exchange, 404, "Not found.");
                logger.warn("Path not found: {}", path);
            }
        } catch (UserNotFoundException e) {
            // Handle user not found errors
            logger.error("User not found. {}", e.getMessage(), e);
            sendResponse(exchange, 404, e.getMessage()); // Handle user not found errors
        } catch (InvalidUserException e) {
            // Handle invalid user data errors
            logger.error("Invalid user data. {}", e.getMessage(), e);
            sendResponse(exchange, 400, e.getMessage()); // Handle invalid user data errors
        } catch (IllegalArgumentException e) {
            // Handle invalid input errors
            logger.error("Invalid input. {}", e.getMessage(), e);
            sendResponse(exchange, 400, "Invalid input: " + e.getMessage()); // Handle invalid input errors
        } catch (Exception e) {
            // Handle unexpected errors
            logger.error("Internal server error. {}", e.getMessage(), e);
            sendResponse(exchange, 500, "Internal server error. " + e.getMessage()); // Handle unexpected errors
        }
    }

    /**
     * Handles requests to the /users endpoint.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param method   The HTTP method (e.g., GET, POST).
     * @throws IOException If an I/O error occurs while handling the request.
     */
    private void handleUsersEndpoint(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                // Retrieve all users from the service
                List<User> users = userService.getAllUsers();

                // Send the list of users as JSON
                sendResponse(exchange, 200, objectMapper.writeValueAsString(users));

                // Log the successful retrieval of all users
                logger.info("Successfully retrieved all users");
                break;
            case "POST":
                // Parse the request body into a User object
                User user = parseRequestBody(exchange, User.class);

                // Validate that required fields are not null or empty
                validateFieldsNotEmpty(
                        user.getName(), "Name",
                        user.getEmail(), "Email",
                        user.getRole() == null ? null : user.getRole().name(), "Role");

                // Create user
                userService.createUser(user);

                // Send a success response and log success
                sendResponse(exchange, 201, "User created successfully");
                logger.info("Successfully created user with ID: {}", user.getId());
                break;
            default:
                // Handle unsupported HTTP methods
                sendResponse(exchange, 405, "Method not allowed.");
                logger.warn("Method not allowed: {}", method);
        }
    }

    /**
     * Handles requests to the /users/{id} endpoint.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param method   The HTTP method (e.g., GET, PUT, DELETE).
     * @param id       The ID of the user to operate on.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    private void handleUserEndpoint(HttpExchange exchange, String method, int id) throws IOException {
        switch (method) {
            case "GET":
                // Retrieve the user by ID from the service
                User user = userService.getUserById(id);

                // Send the user details as JSON and log the success
                sendResponse(exchange, 200, objectMapper.writeValueAsString(user));
                logger.info("Successfully retrieved user with ID: {}", id);
                break;
            case "PATCH":
                // Parse the request body into a User object
                User updatedUser = parseRequestBody(exchange, User.class);

                // Set the ID of the user to update
                updatedUser.setId(id);

                // Update the user
                userService.updateUser(updatedUser);

                // Send success response and log success
                sendResponse(exchange, 200, "User updated successfully");
                logger.info("Successfully updated user with ID: {}", id);
                break;
            case "DELETE":
                // Delete user by ID
                userService.deleteUser(id);

                // Send success response and log success
                sendResponse(exchange, 204, "");
                logger.info("Successfully deleted user with ID: {}", id);
                break;
            default:
                // Handle unsupported HTTP methods
                sendResponse(exchange, 405, "Method not allowed.");
                logger.warn("Method not allowed: {}", method);
        }
    }

    /**
     * Handles search requests to the /users endpoint with query parameters.
     *
     * @param exchange The HttpExchange object representing the HTTP request and
     *                 response.
     * @param query    The query string (e.g., email=user@example.com).
     * @throws IOException If an I/O error occurs while handling the request.
     */
    private void handleSearchUsers(HttpExchange exchange, String query, String method) throws IOException {
        // Handle unsupported HTTP methods
        if (!method.equals("GET")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            logger.warn("Method not allowed for search books endpoint: {}", method);
            return;
        }

        // Parse the query parameters into a map
        Map<String, String> queryParams = parseQueryParameters(query);

        if (queryParams.containsKey("email")) {
            // Search user by email
            String email = queryParams.get("email");

            // Validate the email format
            validateFieldsNotEmpty(email, "Email");

            // Retrieve the user by email from the service
            User user = userService.getUserByEmail(email);

            // Send the user details as JSON and log the success
            sendResponse(exchange, 200, objectMapper.writeValueAsString(user));
            logger.info("Successfully searched user by email: {}", email);
        } else {
            // Handle invalid search parameters
            sendResponse(exchange, 400, "Invalid search parameter");
            logger.warn("Invalid search parameter: {}", query);
        }
    }
}
