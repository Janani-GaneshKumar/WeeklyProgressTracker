package com.janani.contentrecommendation.dto;

import com.janani.contentrecommendation.entity.InteractionType;
import lombok.Data;

@Data
public class InteractionRequestDTO {
    private Long userId;
    private Long contentId;
    private InteractionType interactionType;
    private Long sharedUserId;
}
