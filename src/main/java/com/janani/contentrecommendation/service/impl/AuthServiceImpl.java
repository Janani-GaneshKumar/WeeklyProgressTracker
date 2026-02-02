package com.janani.contentrecommendation.service.impl;

import com.janani.contentrecommendation.dto.LoginRequest;
import com.janani.contentrecommendation.dto.RegisterRequest;
import com.janani.contentrecommendation.dto.UserResponse;
import com.janani.contentrecommendation.entity.Role;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.exception.InvalidCredentialsException;
import com.janani.contentrecommendation.exception.UserNotFoundException;
import com.janani.contentrecommendation.repository.UserRepository;
import com.janani.contentrecommendation.config.JwtUtil;
import com.janani.contentrecommendation.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user with chosen role
     */
    @Override
    //RegisterRequest request - Dto object is passed as the parameter
    public UserResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidCredentialsException("Email already registered: " + request.getEmail());
        }

        // Validate role selection
        if (request.getRole() == null) {
            throw new InvalidCredentialsException("Role must be specified");
        }

        // Create new user entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // user chooses role

        // Save to DB
        userRepository.save(user);

        // Return safe DTO response
        return UserResponse.fromEntity(user);
    }

    /**
     * Login existing user and return JWT token
     */
    @Override
    public String login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password for email: " + request.getEmail());
        }

        // Generate JWT with email + role
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
