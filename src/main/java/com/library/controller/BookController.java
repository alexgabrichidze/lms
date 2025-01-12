package com.library.controller;

import com.library.service.BookServiceImpl;
import com.library.service.BookService;

public class BookController extends BaseController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

}
