package com.library.controller;

import com.library.service.UserService;
import com.library.service.exceptions.InvalidUserException;
import com.library.service.exceptions.UserNotFoundException;

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
    private static UserService userService;

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
                    // handleSearchUsers(exchange, query);
                } else {
                    // handleUsersEndpoint(exchange, method);
                }
            } else if (path.matches("/users/\\d+")) {
                int id = extractIdFromPath(path);

                validatePositiveId(id, "User ID", () -> new InvalidUserException("Invalid user ID"));

                // handleUserEndpoint(exchange, method, id);
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

    
}   
