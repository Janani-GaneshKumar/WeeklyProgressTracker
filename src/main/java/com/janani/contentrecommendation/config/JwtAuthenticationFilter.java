package com.janani.contentrecommendation.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
Each HTTP request:
->Client sends JWT in header
->JwtAuthenticationFilter reads token
->Valid token → create Authentication
->Store Authentication in SecurityContextHolder
->Request completes
->SecurityContext is cleared
-> Next request → starts fresh
-> JWT is stateless
 */
/*
The main Work of this JwtAuthenticationFilter is when a user sends a request (JWT Token), It checks if the
 token is valid, If it is valid it allows the user inside or it  shows unauthorized
 */
@Component
//@Component Registers this filter in Spring’s container
//So it can be auto-injected into SecurityConfig
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /*
    OncePerRequestFilter is a spring security abstract class which makes the Filter is executed only once per http request
     */
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    //This Method is executed everytime when the request comes
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            /*
            SecurityContextHolder - This stores the Authentication Object
            principal → username / email,authorities → roles, details → IP, request info
             */
            if (jwtUtil.validateToken(token)) {
                /*
                UsernamePasswordAuthenticationToken - This is a built in Class That implements Authentication Interface -
                It represents Authenticated user in spring security
                In this block we are creating a Authentication Object and adding extra info about the request
                like Http , Session Id if any
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, jwtUtil.getAuthorities(token));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /*
                 SecurityContextHolder.getContext().setAuthentication(authentication);
                 We are registering this user with Spring Security for this request.
                 */
            }
        }
        filterChain.doFilter(request, response);
        //passing the request to the next filter
    }
}
