package com.example.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Utility class for token validation in Gateway
 * 
 * This class is duplicated from user-service to avoid dependencies.
 * Used for verifying JWT tokens in the authentication filter.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationMustBeLongEnough256Bits!}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * Validate and parse JWT token
     * 
     * @param token JWT token string
     * @return Claims if valid, throws JwtException if invalid
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract userId from token
     * 
     * @param token JWT token string
     * @return userId integer
     */
    public Integer getUserIdFromToken(String token) {
        return parseToken(token).get("userId", Integer.class);
    }

    /**
     * Extract role from token
     * 
     * @param token JWT token string
     * @return role integer
     */
    public Integer getRoleFromToken(String token) {
        return parseToken(token).get("role", Integer.class);
    }

    /**
     * Validate token - check if expired and signature is valid
     * 
     * @param token JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get signing key from secret
     * 
     * @return SecretKey for signing/verification
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
