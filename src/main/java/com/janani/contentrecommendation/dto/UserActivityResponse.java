package com.janani.contentrecommendation.dto;

import lombok.Data;

import java.util.List;
@Data
public class UserActivityResponse {
    private Long userId;
    private String name;
    private List<UserActivityDTO> activities;
}
