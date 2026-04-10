package com.example.user.controller;

import com.example.user.common.Result;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(
            @RequestParam("username") @NotBlank String username,
            @RequestParam("password") @NotBlank String password,
            @RequestParam(value = "phone", required = false) String phone) {
        try {
            User user = userService.register(username, password, phone);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<String> login(
            @RequestParam("username") @NotBlank String username,
            @RequestParam("password") @NotBlank String password) {
        try {
            String token = userService.login(username, password);
            return Result.success(token);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.contains("not found")) {
                return Result.error(404, msg);
            }
            return Result.error(401, msg);
        }
    }

    @GetMapping("/{username}")
    public Result<User> getUser(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error(404, "User not found");
        }
        user.setPassword(null);
        return Result.success(user);
    }
}
