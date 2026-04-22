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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(String username, String phone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new RuntimeException("Username already exists");
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new RuntimeException("Phone already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(username);
        user.setRole(0);
        user.setStatus(0);

        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public String login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        if (user.getStatus() == 1) {
            throw new RuntimeException("User account is disabled");
        }

        return jwtUtil.generateToken(user.getUserId(), user.getNickname(), user.getRole());
    }

    @Override
    public User findByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findByUserId(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User updateUserInfo(Long userId, String nickname, String avatar, String sportPreference, String intro) {
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
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is wrong");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean disableUser(Long userId, Integer status) {
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
        
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(User::getNickname, keyword)
                    .or()
                    .like(User::getPhone, keyword));
        }
        
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        
        return userMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.deleteById(userId) > 0;
    }
}