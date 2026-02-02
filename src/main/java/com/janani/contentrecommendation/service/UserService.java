package com.janani.contentrecommendation.service;

import com.janani.contentrecommendation.dto.PreferenceRequest;
import com.janani.contentrecommendation.dto.UserResponse;

public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse updatePreferences(Long id, PreferenceRequest request);
    UserResponse becomeCurator(Long id);
}
