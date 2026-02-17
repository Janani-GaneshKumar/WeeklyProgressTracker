package com.janani.contentrecommendation.dto;

import com.janani.contentrecommendation.entity.InteractionType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class InteractionResponseDTO {

    private Long interactionId;
    private Long userId;
    private Long contentId;
    private Long curatorId;
    private InteractionType interactionType;
    private LocalDateTime timestamp;
    private String sharedUserId;
}
