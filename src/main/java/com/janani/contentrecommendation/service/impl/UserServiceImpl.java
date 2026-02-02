package com.janani.contentrecommendation.service.impl;
import com.janani.contentrecommendation.dto.PreferenceRequest;
import com.janani.contentrecommendation.dto.UserResponse;
import com.janani.contentrecommendation.entity.Role;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.exception.UserNotFoundException;
import com.janani.contentrecommendation.repository.UserRepository;
import com.janani.contentrecommendation.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse updatePreferences(Long id, PreferenceRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPreferredCategories(request.getCategories());
        user.setPreferredTags(request.getTags());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse becomeCurator(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(Role.CURATOR); // user decides to opt-in
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }
}
