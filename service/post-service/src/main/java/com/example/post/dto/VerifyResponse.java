package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Verify Response DTO
 * Used for token verification endpoint to return user information from user-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResponse {

    /**
     * User ID
     */
    private Long userId;

    /**
     * User nickname
     */
    private String nickname;

    /**
     * User role: 0-normal user, 1-admin
     */
    private Integer role;

    /**
     * User phone number
     */
    private String phone;
}
