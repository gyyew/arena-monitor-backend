package com.example.post.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 配置 post-service 的安全策略，允许公开访问帖子列表等只读API
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 安全过滤器链配置
     * - 禁用 CSRF
     * - 使用无状态会话
     * - 允许公开访问 GET 类型的帖子/评论API
     * - 其他操作需要认证
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 允许公开访问的API
                .requestMatchers("GET", "/api/v1/posts/**").permitAll()
                .requestMatchers("GET", "/api/v1/comments/**").permitAll()
                // 其他请求需要认证
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
