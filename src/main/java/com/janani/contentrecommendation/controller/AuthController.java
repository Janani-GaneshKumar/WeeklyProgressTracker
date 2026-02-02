package com.janani.contentrecommendation.controller;
import com.janani.contentrecommendation.dto.LoginRequest;
import com.janani.contentrecommendation.dto.RegisterRequest;
import com.janani.contentrecommendation.dto.UserResponse;
import com.janani.contentrecommendation.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController //tells Spring this class handles REST API requests and returns JSON responses.
@RequestMapping("/auth")//all endpoint starts with "/"
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(@Qualifier("authServiceImpl") AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/register
    @PostMapping("/register")
    //@RequestBody RegisterRequest request -takes Json from client and converts it into java object
    //@Valid - It applies validation rules (email,username,etc..)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);//It handles service
        return ResponseEntity.ok(response);//It returns JSON response
    }
    // POST /auth/login
    @PostMapping("/login")
    // @RequestBody LoginRequest request - It takes json item email and password
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);//It handles service logic
        return ResponseEntity.ok(token);//it returns as token
    }

}
