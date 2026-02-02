package com.janani.contentrecommendation.controller;
import com.janani.contentrecommendation.dto.PreferenceRequest;
import com.janani.contentrecommendation.dto.UserResponse;
import com.janani.contentrecommendation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','CURATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserResponse> updatePreferences(
            @PathVariable Long id,
            @Valid @RequestBody PreferenceRequest request) {
        return ResponseEntity.ok(userService.updatePreferences(id, request));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/become-curator")
    public ResponseEntity<UserResponse> becomeCurator(@PathVariable Long id) {
        return ResponseEntity.ok(userService.becomeCurator(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("This is a protected profile endpoint!");
    }
}
