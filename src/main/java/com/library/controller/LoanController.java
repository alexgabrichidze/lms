package com.library.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.library.service.LoanService;
import com.library.service.exceptions.InvalidLoanException;
import com.library.service.exceptions.LoanConflictException;
import com.library.service.exceptions.LoanNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import static com.library.util.ValidationUtil.*;

import java.io.IOException;

public class LoanController extends BaseController {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    // LoanService object
    private final LoanService loanService;

    /**
     * Constructs a new LoanController object with a custom LoanService object.
     *
     * @param loanService The LoanService object to be used by the LoanController.
     */
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
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
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {

            // Log the incoming request
            logger.info("Received {} request for path: {}", method, path);

            if (path.matches("/loans")) {
                if (query != null) {
                    // handleSearchLoans(exchange, query);
                } else {
                    // handleLoansEndpoint(exchange, method);
                }
            } else if (path.matches("/loans/\\d+")) {
                int id = extractIdFromPath(path);

                validatePositiveId(id, "Loan ID", () -> new InvalidLoanException("Invalid loan ID"));

                // handleLoanEndpoint(exchange, method, id);
            } else if (path.matches("/loans/active")) {
                // handleActiveLoansEndpoint(exchange, method);
            } else {

                // Handle path not found errors
                sendResponse(exchange, 404, "Not found.");
                logger.warn("Path not found: {}", path);
            }

        } catch (LoanNotFoundException e) {

            // Handle loan not found errors
            logger.error("Loan not found. {}", e.getMessage(), e);
            sendResponse(exchange, 404, e.getMessage()); // Handle loan not found errors
        } catch (InvalidLoanException e) {

            // Handle invalid loan data errors
            logger.error("Invalid loan data. {}", e.getMessage(), e);
            sendResponse(exchange, 400, e.getMessage()); // Handle invalid loan data errors
        } catch (IllegalArgumentException e) {

            // Handle invalid input errors
            logger.error("Invalid input. {}", e.getMessage(), e);
            sendResponse(exchange, 400, "Invalid input: " + e.getMessage()); // Handle invalid input errors
        } catch (LoanConflictException e) {

            // Handle loan conflict errors
            logger.error("Loan conflict. {}", e.getMessage(),
                    e);
            sendResponse(exchange, 409, e.getMessage()); // Handle loan conflict errors
        } catch (Exception e) {

            // Handle unexpected errors
            logger.error("Internal server error. {}", e.getMessage(), e);
            sendResponse(exchange, 500, "Internal server error. " + e.getMessage()); // Handle unexpected errors
        }
    }
}
