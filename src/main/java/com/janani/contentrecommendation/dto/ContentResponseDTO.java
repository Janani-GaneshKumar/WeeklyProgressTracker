package com.janani.contentrecommendation.dto;

import com.janani.contentrecommendation.entity.Content;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ContentResponseDTO {
    private Long id;
    private String title;
    private String category;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public ContentResponseDTO(Content content) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.category = content.getCategory();
        this.url = content.getUrl();
        this.createdAt = content.getCreatedAt();
        this.updatedAt = content.getUpdatedAt();
        this.userId = content.getCurator().getUserId();
    }


}
