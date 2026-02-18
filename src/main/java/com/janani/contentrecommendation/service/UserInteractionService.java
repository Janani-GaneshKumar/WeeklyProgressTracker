package com.janani.contentrecommendation.service;

import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.UserInteraction;

import java.util.List;
import java.util.Map;

public interface UserInteractionService {
        void recordInteraction(Long userId, Long contentId, InteractionType type);
        void recordShare(Long userId, Long contentId, Long sharedUserId);
        List<UserInteraction> getUserActivity(Long userId);
        List<UserInteraction> getUserActivityByType(Long userId, InteractionType type);
        Map<String, List<UserInteraction>> getGroupedUserActivity(Long userId);


}
