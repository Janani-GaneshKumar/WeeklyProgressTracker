package com.janani.contentrecommendation.service;


import com.janani.contentrecommendation.dto.LoginRequest;
import com.janani.contentrecommendation.dto.RegisterRequest;
import com.janani.contentrecommendation.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    String login(LoginRequest request);
}
