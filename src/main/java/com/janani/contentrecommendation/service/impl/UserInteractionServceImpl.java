package com.janani.contentrecommendation.service.impl;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.entity.UserInteraction;
import com.janani.contentrecommendation.repository.UserInteractionRepository;
import com.janani.contentrecommendation.service.UserInteractionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserInteractionServiceImpl implements UserInteractionService {

    private final UserInteractionRepository repository;

    public UserInteractionServiceImpl(UserInteractionRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserInteraction logInteraction(User user, Content content, InteractionType type, String sharePlatform) {
        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
        interaction.setContent(content);
        interaction.setCurator(content.getOwner()); // curator of the content
        interaction.setInteractionType(type);
        interaction.setTimestamp(LocalDateTime.now());

        if (type == InteractionType.SHARE) {
            interaction.setSharePlatform(sharePlatform);
        }

        return repository.save(interaction);
    }

    @Override
    public List<UserInteraction> getInteractionsByUser(Long userId) {
        return repository.findByUser_UserId(userId);
    }

    @Override
    public List<UserInteraction> getInteractionsByContent(Long contentId) {
        return repository.findByContent_Id(contentId);
    }

    @Override
    public List<UserInteraction> getInteractionsByCurator(Long curatorId) {
        return repository.findByCurator_UserId(curatorId);
    }

    @Override
    public List<UserInteraction> getAllInteractions() {
        return repository.findAll();
    }
}
