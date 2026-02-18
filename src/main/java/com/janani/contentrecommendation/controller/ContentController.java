package com.janani.contentrecommendation.controller;
import com.amazonaws.services.s3.AmazonS3;
import com.janani.contentrecommendation.dto.ContentResponseDTO;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.service.ContentService;
import com.janani.contentrecommendation.service.UserInteractionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController//-Marks this class as a REST Controller ,Handles http request and return JSON Reponse
@RequestMapping("/contents")// All endpoints in this request will be prefixed with this
public class ContentController {

    @Value("${aws.s3.bucket}")
    private String bucketName;//Extracting bucket name from the application.properties
    private final ContentService contentService;
    private final UserInteractionService userInteractionService;
    private final AmazonS3 s3Client;// Amazon sdk service to interact with s3

    @Autowired// This ensures dependencies are provided at runtime
    public ContentController(ContentService contentService,
                             UserInteractionService userInteractionService, AmazonS3 s3Client) {
        this.contentService = contentService;
        this.userInteractionService = userInteractionService;
        this.s3Client = s3Client;
    }

    // Upload endpoint// consumes = MediaType.MULTIPART_FORM_DATA_VALUE - this like specifies this endpoint requires multipart file
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContentResponseDTO> createContent(
            @RequestParam("file") MultipartFile file,//extracting file from the request
            @RequestParam("title") String title,//extracting title from the request
            @RequestParam("category") String category,//extracting category from the request
            @RequestParam("userId") Long userId//extracting user id from the reuqest
             ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);//if empty it return 400 bad request with no body
        }

        Content content = contentService.uploadContent(file, title, category, userId);//calls the upload content method in my contentservice
        //it returns content entity
        return ResponseEntity.ok(new ContentResponseDTO(content));// returns 200 ok with dto as json
    }

    // Get content by ID
    @GetMapping("/{id}")
    //PathVariable binds the Id from url directly to the parameter
    public ResponseEntity<ContentResponseDTO> getContentById(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        return ResponseEntity.ok(new ContentResponseDTO(content));
        //Wraps the entity in a DTO and returns it as a JSON response with 200 OK
    }

    // Get contents by category
    @GetMapping("/category/{category}")
    //ResponseEntity is a Spring class that represents the entire HTTP response.
    //ResponseEntity controls status codes,header and body
    public ResponseEntity<List<ContentResponseDTO>> getContentsByCategory(@PathVariable String category) {
        List<Content> contents = contentService.getContentsByCategory(category);
        return ResponseEntity.ok(
                contents.stream()//Converts the list of Content entities into a stream for processing.
                        .map(ContentResponseDTO::new)//For each Content entity, creates a new ContentResponseDTO. This transforms raw database entities into clean DTOs for the client.
                        .collect(Collectors.toList())//Collects the transformed DTOs back into a list.
        );
    }

    // Delete content
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.ok("Content deleted successfully");
    }

    @GetMapping("/getfile/{filename}")
    public void streamFile(@PathVariable String filename,
                           @RequestHeader(value = "Range", required = false) String rangeHeader,
                           HttpServletResponse response) {
        contentService.streamFile(filename, response, rangeHeader);
    }



}
