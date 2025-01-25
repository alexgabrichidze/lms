package com.library;

import com.library.controller.*;
import com.library.dao.*;
import com.library.service.*;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            // Initialize container with all dependencies
            AppContainer container = new AppContainer();

            // Create server with controllers from container
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/books", container.bookController());
            server.createContext("/users", container.userController());
            server.createContext("/loans", container.loanController());

            server.start();
            logger.info("Server started on port 8080");

        } catch (IOException e) {
            logger.error("Failed to start server: ", e);
        }
    }

    /**
     * Dependency container that manages object creation and wiring
     */
    private static class AppContainer {
        private final BookController bookController;
        private final UserController userController;
        private final LoanController loanController;

        public AppContainer() {
            // Initialize core dependencies first
            BookDao bookDao = new BookDaoImpl();
            UserDao userDao = new UserDaoImpl();
            LoanDao loanDao = new LoanDaoImpl();

            // Wire services
            BookService bookService = new BookServiceImpl(bookDao);
            UserService userService = new UserServiceImpl(userDao);
            LoanService loanService = new LoanServiceImpl(loanDao, bookService, userService);

            // Create controllers
            this.bookController = new BookController(bookService);
            this.userController = new UserController(userService);
            this.loanController = new LoanController(loanService);
        }

        // Getters for controllers
        public BookController bookController() {
            return bookController;
        }

        public UserController userController() {
            return userController;
        }

        public LoanController loanController() {
            return loanController;
        }
    }
}