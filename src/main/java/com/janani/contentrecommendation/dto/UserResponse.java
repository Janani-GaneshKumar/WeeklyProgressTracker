package com.janani.contentrecommendation.dto;

import com.janani.contentrecommendation.entity.User;

public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String preferredCategories;
    private String preferredTags;

    // Static factory method to convert Entity → DTO
    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setPreferredCategories(user.getPreferredCategories());
        response.setPreferredTags(user.getPreferredTags());
        return response;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(String preferredCategories) { this.preferredCategories = preferredCategories; }

    public String getPreferredTags() { return preferredTags; }
    public void setPreferredTags(String preferredTags) { this.preferredTags = preferredTags; }
}
