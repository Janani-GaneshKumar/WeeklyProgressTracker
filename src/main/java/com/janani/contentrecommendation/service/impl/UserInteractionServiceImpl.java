package com.janani.contentrecommendation.service.impl;
import com.janani.contentrecommendation.entity.*;
import com.janani.contentrecommendation.repository.ContentRepository;
import com.janani.contentrecommendation.repository.UserRepository;
import com.janani.contentrecommendation.repository.UserInteractionRepository;
import com.janani.contentrecommendation.service.UserInteractionService;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserInteractionServiceImpl implements UserInteractionService {

    private final UserInteractionRepository interactionRepo;
    private final UserRepository userRepo;
    private final ContentRepository contentRepo;

    public UserInteractionServiceImpl(UserInteractionRepository interactionRepo,
                                      UserRepository userRepo,
                                      ContentRepository contentRepo) {
        this.interactionRepo = interactionRepo;
        this.userRepo = userRepo;
        this.contentRepo = contentRepo;
    }

    @Override
    public void recordInteraction(Long userId, Long contentId, InteractionType type) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Content content = contentRepo.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        if (!interactionRepo.existsByUser_UserIdAndContent_IdAndInteractionType(userId, contentId, type)) {
            UserInteraction interaction = new UserInteraction();
            interaction.setUser(user);
            interaction.setContent(content);
            interaction.setInteractionType(type);
            interactionRepo.save(interaction);
        }
    }

    @Override
    public void recordShare(Long userId, Long contentId, Long sharedUserId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Content content = contentRepo.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));
        User sharedUser = userRepo.findById(sharedUserId)
                .orElseThrow(() -> new RuntimeException("Shared user not found"));

        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
        interaction.setContent(content);
        interaction.setInteractionType(InteractionType.SHARE);
        interaction.setSharedUser(sharedUser);
        interactionRepo.save(interaction);
    }

    @Override
    public List<UserInteraction> getUserActivity(Long userId) {
        return interactionRepo.findByUser_UserId(userId);
    }

    public List<UserInteraction> getUserActivityByType(Long userId, InteractionType type) {
        return interactionRepo.findByUser_UserIdAndInteractionType(userId, type);
    }

    public Map<String, List<UserInteraction>> getGroupedUserActivity(Long userId) {
        List<UserInteraction> interactions = interactionRepo.findByUser_UserId(userId);

        Map<String, List<UserInteraction>> grouped = new HashMap<>();
        grouped.put("views", interactions.stream()
                .filter(i -> i.getInteractionType() == InteractionType.VIEW)
                .collect(Collectors.toList()));
        grouped.put("likes", interactions.stream()
                .filter(i -> i.getInteractionType() == InteractionType.LIKE)
                .collect(Collectors.toList()));
        grouped.put("bookmarks", interactions.stream()
                .filter(i -> i.getInteractionType() == InteractionType.BOOKMARK)
                .collect(Collectors.toList()));
        grouped.put("shares", interactions.stream()
                .filter(i -> i.getInteractionType() == InteractionType.SHARE)
                .collect(Collectors.toList()));

        return grouped;
    }

}
