package com.example.fixmate.utils;

import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET = "rakesh-rakesh-rakesh-rakesh123456"; // at least 32 chars
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role) {
        System.out.println("user name while generate token" + username);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 60)) // 1 hour
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // ✅ correct way
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // ✅ correct parser usage
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // ✅ correct parser usage
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);

    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
