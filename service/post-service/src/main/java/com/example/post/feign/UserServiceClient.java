package com.example.post.feign;

import com.example.post.common.UserResult;
import com.example.post.dto.VerifyResponse;
import com.example.post.feign.config.UserServiceFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * User Service Feign Client
 * Used for inter-service communication with user-service
 */
@FeignClient(
    name = "user-service",
    url = "${feign.client.user-service.url:http://localhost:8082}",
    configuration = UserServiceFeignConfig.class
)
public interface UserServiceClient {

    /**
     * Verify JWT token and return user information
     * @param authorization Authorization header containing Bearer token
     * @return user information wrapped in Result if token is valid
     */
    @GetMapping("/api/v1/users/verify")
    UserResult<VerifyResponse> verifyToken(@RequestHeader("Authorization") String authorization);
}
