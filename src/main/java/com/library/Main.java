package com.library;

import com.library.controller.BookController;
import com.library.dao.*;
import com.library.service.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main class to start the HTTP server and register the BookController.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Initialize the BookService and BookController
            BookDao bookDao = new BookDaoImpl(); // Assuming BookDao is implemented
            BookService bookService = new BookServiceImpl(bookDao); // Initialize BookService with BookDao
            BookController bookController = new BookController(bookService); // Initialize BookController with
                                                                             // BookService

            // Create an HTTP server listening on port 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Register the BookController to handle requests to the /books path
            server.createContext("/books", bookController);

            // Start the server
            server.start();
            System.out.println("Server started on port 8080. Access the API at http://localhost:8080/books");

        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}