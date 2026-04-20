package com.example.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.user.common.Result;
import com.example.user.dto.VerifyResponse;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import com.example.user.util.JwtUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<User> register(
            @RequestParam("phone") @NotBlank String phone,
            @RequestParam("password") @NotBlank String password,
            @RequestParam("nickname") @NotBlank String nickname) {
        try {
            User user = userService.register(phone, password, nickname);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<String> login(
            @RequestParam("phone") @NotBlank String phone,
            @RequestParam("password") @NotBlank String password) {
        try {
            String token = userService.login(phone, password);
            return Result.success(token);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.contains("not found")) {
                return Result.error(404, msg);
            }
            return Result.error(401, msg);
        }
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        // Clear security context - token invalidation handled client-side
        SecurityContextHolder.clearContext();
        return Result.success("Logged out successfully");
    }

    /**
     * Verify JWT token and return user information
     * This endpoint is permitAll - no authentication required
     * @param authorization Authorization header containing Bearer token
     * @return user information if token is valid
     */
    @GetMapping("/verify")
    public Result<VerifyResponse> verifyToken(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        // Check if Authorization header is present
        if (authorization == null || authorization.isEmpty()) {
            return Result.error(401, "Authorization header is missing");
        }

        // Extract token from Bearer format
        String token;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        } else {
            token = authorization;
        }

        // Validate and parse token
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "Invalid or expired token");
        }

        try {
            // Extract user information from token
            Integer userId = jwtUtil.getUserIdFromToken(token);
            String nickname = jwtUtil.getNicknameFromToken(token);
            Integer role = jwtUtil.getRoleFromToken(token);

            // Get phone from database
            User user = userService.findByUserId(userId);
            if (user == null) {
                return Result.error(404, "User not found");
            }

            // Build response
            VerifyResponse response = new VerifyResponse(userId, nickname, role, user.getPhone());
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(401, "Token parsing failed: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            Integer userId = (Integer) authentication.getPrincipal();
            User user = userService.findByUserId(userId);
            if (user == null) {
                return Result.error(404, "User not found");
            }
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error(401, "Not authenticated");
    }

    @PutMapping("/me")
    public Result<User> updateUserInfo(
            @RequestParam("nickname") @NotBlank String nickname,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "sportPreference", required = false) String sportPreference,
            @RequestParam(value = "intro", required = false) String intro) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Integer userId = (Integer) authentication.getPrincipal();
            User user = userService.updateUserInfo(userId, nickname, avatar, sportPreference, intro);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/me/password")
    public Result<String> changePassword(
            @RequestParam("oldPassword") @NotBlank String oldPassword,
            @RequestParam("newPassword") @NotBlank String newPassword) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Integer userId = (Integer) authentication.getPrincipal();
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                return Result.success("Password changed successfully");
            } else {
                return Result.error(400, "Password change failed");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/admin/disable/{userId}")
    public Result<String> disableUser(
            @PathVariable("userId") Integer userId,
            @RequestParam("status") Integer status) {
        try {
            boolean success = userService.disableUser(userId, status);
            if (success) {
                return Result.success("User status updated successfully");
            } else {
                return Result.error(400, "User status update failed");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/admin/{userId}")
    public Result<User> getUserById(@PathVariable("userId") Integer userId) {
        try {
            User user = userService.findByUserId(userId);
            if (user == null) {
                return Result.error(404, "User not found");
            }
            user.setPassword(null);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * Get user list with pagination and search
     * @param page page number, default 1
     * @param size page size, default 10
     * @param keyword search keyword for nickname or phone
     * @param status user status filter, optional
     * @return paginated user list
     */
    @GetMapping
    public Result<IPage<User>> getUserList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {
        try {
            IPage<User> userPage = userService.getUserList(page, size, keyword, status);
            // Clear password for security
            userPage.getRecords().forEach(u -> u.setPassword(null));
            return Result.success(userPage);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * Delete user by ID (admin only)
     * @param userId user ID to delete
     * @return success message
     */
    @DeleteMapping("/admin/{userId}")
    public Result<String> deleteUser(@PathVariable("userId") Integer userId) {
        try {
            boolean success = userService.deleteUser(userId);
            if (success) {
                return Result.success("User deleted successfully");
            } else {
                return Result.error(404, "User not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}