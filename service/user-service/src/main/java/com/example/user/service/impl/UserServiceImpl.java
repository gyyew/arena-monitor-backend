package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.user.entity.User;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import com.example.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public User register(String phone, String password, String nickname) {
        User existUser = findByPhone(phone);
        if (existUser != null) {
            throw new RuntimeException("Phone already registered");
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(md5Encrypt(password));
        user.setNickname(nickname);
        user.setRole(0); // 默认普通用户
        user.setStatus(0); // 默认正常状态

        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public String login(String phone, String password) {
        User user = findByPhone(phone);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(md5Encrypt(password))) {
            throw new RuntimeException("Wrong password");
        }

        if (user.getStatus() == 1) {
            throw new RuntimeException("User account is disabled");
        }

        // Generate JWT token
        return jwtUtil.generateToken(user.getUserId(), user.getNickname(), user.getRole());
    }

    @Override
    public User findByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User updateUserInfo(Integer userId, String nickname, String avatar, String sportPreference, String intro) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setNickname(nickname);
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        if (sportPreference != null) {
            user.setSportPreference(sportPreference);
        }
        if (intro != null) {
            user.setIntro(intro);
        }

        userMapper.updateById(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(md5Encrypt(oldPassword))) {
            throw new RuntimeException("Old password is wrong");
        }

        user.setPassword(md5Encrypt(newPassword));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean disableUser(Integer userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setStatus(status);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public IPage<User> getUserList(int page, int size, String keyword, Integer status) {
        Page<User> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        // Add keyword search condition (search in nickname or phone)
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(User::getNickname, keyword)
                    .or()
                    .like(User::getPhone, keyword));
        }
        
        // Add status filter
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        // Order by create time desc
        wrapper.orderByDesc(User::getCreateTime);
        
        return userMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public boolean deleteUser(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.deleteById(userId) > 0;
    }

    // MD5加密
    private String md5Encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 encryption failed", e);
        }
    }
}