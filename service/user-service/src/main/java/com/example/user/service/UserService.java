package com.example.user.service;

import com.example.user.entity.User;

public interface UserService {

    User register(String phone, String password, String nickname);

    String login(String phone, String password);

    User findByPhone(String phone);

    User findByUserId(Integer userId);

    User updateUserInfo(Integer userId, String nickname, String avatar, String sportPreference, String intro);

    boolean changePassword(Integer userId, String oldPassword, String newPassword);

    boolean disableUser(Integer userId, Integer status);
}
