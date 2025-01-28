package com.library;

import com.library.controller.*;
import com.library.dao.*;
import com.library.service.*;
import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Configurable pool size
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        final ExecutorService executor;
        final HttpServer server;
        AppContainer container;

        try {
            // Initialize container with all dependencies
            container = new AppContainer();

            // Create server with controllers from container
            server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Configure thread pool
            executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            server.setExecutor(executor);

            // Set up endpoints
            server.createContext("/books", container.bookController());
            server.createContext("/users", container.userController());
            server.createContext("/loans", container.loanController());

            server.start();
            logger.info("Server started on port 8080 with {} threads", THREAD_POOL_SIZE);

            // Add shutdown hook for graceful termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Initiating graceful shutdown...");
                server.stop(0);
                executor.shutdownNow();
                logger.info("Application shutdown completed.");
            }));

        } catch (IOException e) {
            logger.error("Server error: ", e);
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