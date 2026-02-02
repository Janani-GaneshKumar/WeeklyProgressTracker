package com.janani.contentrecommendation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContentRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Body is required")
    private String body;
    @NotBlank(message = "Author email is required")
    private String authorEmail;
    @NotNull(message = "Content type is required")
    private String type;// ARTICLE, VIDEO, IMAGE private boolean published;

    //have to add getter and setter methods

}
