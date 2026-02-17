package com.janani.contentrecommendation.dto;

import lombok.Data;

@Data
public class UserActivityDTO {
    private Long interactionId;
    private String type;
    private String contentTitle;
    private String contentCategory;
    private String timestamp;
    private String sharedWith;
}
