package com.example.post.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Result wrapper class for user-service API responses
 * This mirrors the Result class in user-service for Feign deserialization
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Response code (200 for success)
     */
    private Integer code;

    /**
     * Response message
     */
    private String msg;

    /**
     * Response data
     */
    private T data;

    /**
     * Request ID for tracing
     */
    private String requestId;

    /**
     * Check if the response is successful
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
