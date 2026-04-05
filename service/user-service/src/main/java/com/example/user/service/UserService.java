package com.example.user.service;

import com.example.user.entity.User;

public interface UserService {

    User register(String username, String password, String phone);

    String login(String username, String password);

    User findByUsername(String username);
}
