package com.example.post.feign.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign Client Configuration for user-service
 * Explicitly configures the UserServiceClient bean
 */
@Configuration
public class UserServiceFeignConfig {

    /**
     * Configure request timeout for user-service Feign client
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(5000, TimeUnit.MILLISECONDS, 10000, TimeUnit.MILLISECONDS, true);
    }

    /**
     * Configure logging level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
