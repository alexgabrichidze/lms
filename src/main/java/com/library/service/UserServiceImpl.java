package com.library.service;

import com.library.dao.UserDao;
import com.library.dao.UserDaoImpl;
import com.library.model.User;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public void createUser(User user) {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidUserException("User name cannot be null or empty.");
        }
    }
}
