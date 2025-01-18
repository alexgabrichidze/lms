package com.library.controller;

import com.library.service.UserService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
}
