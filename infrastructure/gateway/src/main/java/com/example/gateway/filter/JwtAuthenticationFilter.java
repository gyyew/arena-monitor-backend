package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Global JWT Authentication Filter
 * 
 * Intercepts all incoming requests and validates JWT tokens.
 * - Validates Authorization header with Bearer token
 * - Returns 401 for invalid/expired tokens
 * - Allows certain public paths without authentication
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Public paths that don't require authentication
     */
    private static final List<String> WHITELIST = List.of(
            "/api/v1/users/login",
            "/api/v1/users/register",
            "/api/v1/users/verify",
            "/api/v1/courts"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Check if path is in whitelist
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // Get Authorization header
        String authHeader = request.getHeaders().getFirst("Authorization");

        // Validate Authorization header format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse(), "Missing or invalid Authorization header");
        }

        // Extract token from Bearer scheme
        String token = authHeader.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            return unauthorized(exchange.getResponse(), "Invalid or expired token");
        }

        // Token is valid, add user info to headers for downstream services
        try {
            Integer userId = jwtUtil.getUserIdFromToken(token);
            Integer role = jwtUtil.getRoleFromToken(token);

            // Add custom headers for downstream services
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId != null ? userId.toString() : "")
                    .header("X-User-Role", role != null ? role.toString() : "")
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            return unauthorized(exchange.getResponse(), "Token parsing error");
        }
    }

    /**
     * Check if the path is in the whitelist
     */
    private boolean isWhitelisted(String path) {
        return WHITELIST.stream().anyMatch(path::startsWith);
    }

    /**
     * Return 401 Unauthorized response
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        String body = "{\"code\":401,\"message\":\"" + message + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        // Execute after RouteLocator filter but before other filters
        return -100;
    }
}
