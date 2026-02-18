package com.janani.contentrecommendation.service;
import com.janani.contentrecommendation.entity.Content;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
public interface ContentService {
    Content uploadContent(MultipartFile file, String title, String category, Long userId);
    Content getContentById(Long id);
    List<Content> getContentsByCategory(String category);
    void deleteContent(Long id);
    public void streamFile(String filename, HttpServletResponse response, String rangeHeader);
}
