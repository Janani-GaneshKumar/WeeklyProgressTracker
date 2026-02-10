package com.janani.contentrecommendation.repository;

import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    List<UserInteraction> findByUser_UserId(Long userId);
    List<UserInteraction> findByContent_Id(Long contentId);
    List<UserInteraction> findByCurator_UserId(Long curatorId);
    List<UserInteraction> findByInteractionType(InteractionType type);
}
