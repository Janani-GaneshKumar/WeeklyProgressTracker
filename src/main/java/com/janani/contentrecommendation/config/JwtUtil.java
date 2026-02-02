package com.janani.contentrecommendation.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // Generate JWT with role claim
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extract username (subject)
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate token: signature + expiration
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);

            // Explicit expiration check
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            // Optional: check subject is not null
            return claims.getSubject() != null;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid signature, malformed token, etc.
        }
    }

    // Extract authorities from role claim
    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = extractClaims(token);
        String role = claims.get("role", String.class);
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Centralized claim extraction
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
