package com.library;

import com.library.controller.*;
import com.library.dao.*;
import com.library.service.*;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main class to start the HTTP server and register the BookController.
 */
public class Main {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {

            // Initialize the BookService and BookController
            BookDao bookDao = new BookDaoImpl(); // Assuming BookDao is implemented
            BookService bookService = new BookServiceImpl(bookDao); // Initialize BookService with BookDao
            BookController bookController = new BookController(bookService); // Initialize BookController with
                                                                             // BookService

            // Initialize the UserService and UserController
            UserDao userDao = new UserDaoImpl();
            UserService userService = new UserServiceImpl(userDao); // Initialize UserService with UserDao
            UserController userController = new UserController(userService); // Initialize UserController with
                                                                             // UserService

            // Initialize the LoanService and LoanController
            LoanDao loanDao = new LoanDaoImpl();
            LoanService loanService = new LoanServiceImpl(loanDao, bookService, userService); // Initialize LoanService
                                                                                              // with LoanDao,
                                                                                              // BookService, and
                                                                                              // UserService
            LoanController loanController = new LoanController(loanService); // Initialize LoanController with
                                                                             // LoanService

            // Create an HTTP server listening on port 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Register the BookController to handle requests to the /books path
            server.createContext("/books", bookController);

            // Register the UserController to handle requests to the /users path
            server.createContext("/users", userController);

            // Register the LoanController to handle requests to the /loans path
            server.createContext("/loans", loanController);

            // Start the server
            server.start();
            logger.info("Server started on port 8080.");

        } catch (IOException e) {
            logger.error("Failed to start the server: {}", e.getMessage());
        }
    }
}