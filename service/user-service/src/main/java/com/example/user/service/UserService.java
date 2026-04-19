package com.example.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.user.entity.User;

public interface UserService {

    User register(String phone, String password, String nickname);

    String login(String phone, String password);

    User findByPhone(String phone);

    User findByUserId(Integer userId);

    User updateUserInfo(Integer userId, String nickname, String avatar, String sportPreference, String intro);

    boolean changePassword(Integer userId, String oldPassword, String newPassword);

    boolean disableUser(Integer userId, Integer status);

    /**
     * Get user list with pagination and search
     * @param page page number
     * @param size page size
     * @param keyword search keyword for nickname or phone
     * @param status user status filter
     * @return paginated user list
     */
    IPage<User> getUserList(int page, int size, String keyword, Integer status);

    /**
     * Delete user by ID
     * @param userId user ID to delete
     * @return true if deleted successfully
     */
    boolean deleteUser(Integer userId);
}
