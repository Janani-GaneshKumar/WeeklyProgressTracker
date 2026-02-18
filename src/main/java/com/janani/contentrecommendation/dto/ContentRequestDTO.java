package com.janani.contentrecommendation.dto;

import lombok.Data;

@Data
public class ContentRequestDTO {
    private String title;
    private String category;
    private Long userId;

    public ContentRequestDTO() {
    }

    public ContentRequestDTO(String title, String category, Long userId) {
        this.title = title;
        this.category = category;
        this.userId = userId;
    }

}