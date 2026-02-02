package com.janani.contentrecommendation.repository;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Find content by category
    List<Content> findByCategory(String category);

    // Find content by type ARTICLE, PODCAST, VIDEO
    List<Content> findByType(ContentType type);

    // Search content by title keyword case-insensitive
    List<Content> findByTitleContainingIgnoreCase(String keyword);

    // Search content by tags stored as JSON string
    List<Content> findByTagsContaining(String tag);

    // Find content uploaded to a specific S3 URL
    List<Content> findByS3Url(String s3Url);
}
