package com.janani.contentrecommendation.service.impl;

import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.exception.ContentNotFoundException;
import com.janani.contentrecommendation.exception.FileStorageException;
import com.janani.contentrecommendation.exception.UserNotFoundException;
import com.janani.contentrecommendation.repository.ContentRepository;
import com.janani.contentrecommendation.repository.UserRepository;
import com.janani.contentrecommendation.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    // External folder for uploads
    private static final String UPLOAD_DIR = "uploads/";

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public ContentServiceImpl(ContentRepository contentRepository, UserRepository userRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Content uploadContent(MultipartFile file, String title, String category, Long userId) {
        if (file == null || file.isEmpty()) {
            logger.error("Upload failed: empty file provided");
            throw new FileStorageException("Uploaded content file cannot be empty");
        }

        try {
            Path dir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(dir);

            // Add timestamp to filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String originalName = file.getOriginalFilename();
            String fileName = timestamp + "-" + originalName;

            Path filePath = dir.resolve(fileName);
            Files.write(filePath, file.getBytes());
            logger.info("File saved successfully at {}", filePath);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("User not found with id {}", userId);
                        return new UserNotFoundException("User not found with id: " + userId);
                    });

            String fileUrl = "/uploads/" + fileName;

            Content content = new Content(title, category, fileUrl, user);
            content.setCreatedAt(LocalDateTime.now());
            content.setUpdatedAt(LocalDateTime.now());

            Content savedContent = contentRepository.save(content);
            logger.info("Content saved successfully with id {}", savedContent.getId());

            return savedContent;

        } catch (Exception e) {
            logger.error("Failed to upload content: {}", e.getMessage());
            throw new FileStorageException("Failed to upload content", e);
        }
    }

    @Override
    public Content getContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Content not found with id {}", id);
                    return new ContentNotFoundException("Content not found with id: " + id);
                });
    }

    @Override
    public List<Content> getContentsByCategory(String category) {
        logger.info("Fetching contents by category: {}", category);
        return contentRepository.findByCategory(category);
    }

    @Override
    public void deleteContent(Long id) {
        Content content = getContentById(id);
        try {
            if (content.getUrl() != null) {
                Path filePath = Paths.get(UPLOAD_DIR).resolve(
                        Paths.get(content.getUrl()).getFileName().toString()
                );
                Files.deleteIfExists(filePath);
                logger.info("File deleted from path {}", filePath);
            }
        } catch (Exception e) {
            logger.warn("Failed to delete file from disk: {}", e.getMessage());
        }
        contentRepository.deleteById(id);
        logger.info("Content deleted successfully with id {}", id);
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            /*
             Paths.get(UPLOAD_DIR) → base directory where files are stored.
            .resolve(filename) → appends the filename to the directory path.
            .normalize() → cleans the path (removes ../, extra slashes)
             Prevents path traversal attacks and keeps the path safe.
             */
            Resource resource = new UrlResource(filePath.toUri());
            /*
            Converts the file path into a URI.
            UrlResource allows Spring to treat the file as a downloadable/streamable resource.
             */

            if (!resource.exists()) {
                logger.error("File not found: {}", filename);
                return ResponseEntity.notFound().build(); // return a ResponseEntity
            }//check the requested file exists or not

            String contentType;
            try {
                /*
            probeContentType() is a method which tries to guess the MIME type*/
                contentType = Files.probeContentType(filePath);
            } catch (Exception ex) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            /*
            If MIME detection fails:
            Uses application/octet-stream
            This means generic binary data
            Browser will still download/handle it safely
             */
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))//Tells the browser what kind of file is being returned.
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    /*
                    Controls how the browser handles the file:
                    inline → display in browser if possible (PDF, image)
                    filename="..." → suggests the file name
                    If you used attachment instead of inline, it would force download.
                     */
                    .body(resource); // wrap resource in ResponseEntity

        } catch (Exception e) {
            logger.error("Failed to serve file {}: {}", filename, e.getMessage());
            return ResponseEntity.internalServerError().build(); // return ResponseEntity
        }
    }



}
