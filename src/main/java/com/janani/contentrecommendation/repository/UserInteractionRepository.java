package com.janani.contentrecommendation.repository;
import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    List<UserInteraction> findByUser_UserId(Long userId);
    List<UserInteraction> findByContent_Id(Long contentId);
    boolean existsByUser_UserIdAndContent_IdAndInteractionType(Long userId, Long contentId, InteractionType type);
    List<UserInteraction> findByUser_UserIdAndInteractionType(Long userId, InteractionType type);

}
