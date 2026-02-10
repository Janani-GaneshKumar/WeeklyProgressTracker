package com.janani.contentrecommendation.controller;
import com.janani.contentrecommendation.dto.ContentResponseDTO;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.service.ContentService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    // Upload endpoint
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContentResponseDTO> createContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("userId") Long userId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Content content = contentService.uploadContent(file, title, category, userId);
        return ResponseEntity.ok(new ContentResponseDTO(content));
    }

    // Get content by ID
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDTO> getContentById(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        return ResponseEntity.ok(new ContentResponseDTO(content));
    }

    // Get contents by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ContentResponseDTO>> getContentsByCategory(@PathVariable String category) {
        List<Content> contents = contentService.getContentsByCategory(category);
        return ResponseEntity.ok(
                contents.stream()
                        .map(ContentResponseDTO::new)
                        .collect(Collectors.toList())
        );
    }

    // Delete content
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.ok("Content deleted successfully");
    }

    // New endpoint: serve uploaded files
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return contentService.getFile(filename);
    }
}
