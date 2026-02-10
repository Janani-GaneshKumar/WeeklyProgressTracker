package com.janani.contentrecommendation.repository;

import com.janani.contentrecommendation.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Custom finder methods
    List<Content> findByCategory(String category);

    List<Content> findByTitleContainingIgnoreCase(String title);
}
