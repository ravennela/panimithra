package com.example.fixmate.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.fixmate.utils.JwtUtil;
import com.example.fixmate.utils.exceptions.UnAutherizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("added");
        String authHeader = request.getHeader("Authorization");
        System.out.println("auth header" + authHeader);
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                System.out.println("auth key present");
                String jwt = authHeader.substring(7);
                String username = jwtUtil.extractUsername(jwt);
                String role = jwtUtil.extractRole(jwt);

                if (username != null && jwtUtil.validateToken(jwt, username) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("user name present" + username);
                    Collection<? extends GrantedAuthority> authorities = Collections
                            .singletonList(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
                            authorities);

                    System.out.println("authentication completed");

                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        } catch (UnAutherizedException e) {
            // Custom exception you throw in JwtUtil
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"JWT Token has expired or is invalid\"}");
            return; // sto
        } catch (Exception e) {
            // Any other unexpected error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
