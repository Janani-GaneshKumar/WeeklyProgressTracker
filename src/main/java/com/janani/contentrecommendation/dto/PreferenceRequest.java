package com.janani.contentrecommendation.dto;

public class PreferenceRequest {

    private String categories; // JSON or comma-separated string
    private String tags;       // JSON or comma-separated string

    // Getters and Setters
    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}
