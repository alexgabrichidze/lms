package com.library.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.library.service.LoanService;

import com.sun.net.httpserver.HttpExchange;

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

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
