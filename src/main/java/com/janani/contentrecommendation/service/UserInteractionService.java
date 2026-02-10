package com.janani.contentrecommendation.service;

import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.entity.UserInteraction;

import java.util.List;

public interface UserInteractionService {
        UserInteraction logInteraction(User user, Content content, InteractionType type, String sharePlatform);
        List<UserInteraction> getInteractionsByUser(Long userId);
        List<UserInteraction> getInteractionsByContent(Long contentId);
        List<UserInteraction> getInteractionsByCurator(Long curatorId);
        List<UserInteraction> getAllInteractions(); // for admin


}
