
package com.example.rbac.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT secret must be configured");
        }

        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 32 bytes long");
        }

        if (expirationMs <= 0) {
            throw new IllegalStateException(
                    "JWT expiration must be greater than 0");
        }

        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));

        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails user) {

        Date now = new Date();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return parse(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails user) {

        try {
            String username = extractUsername(token);

            Date expiration = parse(token)
                    .getPayload()
                    .getExpiration();

            return username.equals(user.getUsername())
                    && expiration != null
                    && expiration.after(new Date());

        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
