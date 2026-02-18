package com.janani.contentrecommendation.service.impl;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.exception.ContentNotFoundException;
import com.janani.contentrecommendation.exception.FileStorageException;
import com.janani.contentrecommendation.exception.UserNotFoundException;
import com.janani.contentrecommendation.repository.ContentRepository;
import com.janani.contentrecommendation.repository.UserRepository;
import com.janani.contentrecommendation.service.ContentService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
    //logger: For logging info, warnings, and errors.

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository,
                              UserRepository userRepository,
                              AmazonS3 s3Client) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.s3Client = s3Client;
    }

    @Override
    public Content uploadContent(MultipartFile file, String title, String category, Long userId) {

        if (file == null || file.isEmpty()) {
            logger.error("Upload failed: empty file provided");
            throw new FileStorageException("Uploaded content file cannot be empty");
        }

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("User not found with id {}", userId);
                        return new UserNotFoundException("User not found with id: " + userId);
                    });

            // Add timestamp to filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String originalName = file.getOriginalFilename();
            String fileName = timestamp + "-" + originalName;

            // Upload to S3
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            // file.getInputStream(), - Actual data that got read from the maultipart file that was uploaded
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
            logger.info("File uploaded successfully to S3 with key {}", fileName);

            // Generate S3 file URL
            String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

            Content content = new Content(title, category, fileUrl, user);
            content.setCreatedAt(LocalDateTime.now());
            content.setUpdatedAt(LocalDateTime.now());

            Content savedContent = contentRepository.save(content);
            logger.info("Content saved successfully with id {}", savedContent.getId());

            return savedContent;//returning as entity

        } catch (IOException e) {
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
                // Extract the key name from the URL
                String keyName = content.getUrl().substring(content.getUrl().lastIndexOf("/") + 1);
                s3Client.deleteObject(bucketName, keyName);
                logger.info("File deleted from S3 with key {}", keyName);
            }
        } catch (Exception e) {
            logger.warn("Failed to delete file from S3: {}", e.getMessage());
        }
        contentRepository.deleteById(id);
        logger.info("Content deleted successfully with id {}", id);
    }

    public void streamFile(String filename, HttpServletResponse response, String rangeHeader) {
        try {
            ObjectMetadata metadata = s3Client.getObjectMetadata(bucketName, filename);
            long fileLength = metadata.getContentLength();

            long start = 0;
            long end = fileLength - 1;

            boolean isPartial = false;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                isPartial = true;
                String[] ranges = rangeHeader.substring(6).split("-"); //bytes=500-1000->["500","1000"]
                start = Long.parseLong(ranges[0]);// change the first part into a number

                if (ranges.length > 1 && !ranges[1].isEmpty()) {//check if there is another value in range if it is present it will be converted into a number
                    end = Long.parseLong(ranges[1]);
                }
                end = Math.min(end, fileLength - 1);//ensures that the end value doesn't exceed the file length
            }
            //Creates a request to S3 for the file (filename) inside the given bucket.
            GetObjectRequest rangeRequest = new GetObjectRequest(bucketName, filename).withRange(start, end);
            try (S3Object s3Object = s3Client.getObject(rangeRequest);//fetches the s3 oobject from the buket
                 S3ObjectInputStream inputStream = s3Object.getObjectContent()) //this allows you to directly stream the file directly from the bucket
            {

                if (isPartial) {
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//Sets the HTTP response status to 206 Partial Content.
                    response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);//Adds a Content-Range header to the response.
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }

                response.setContentType(metadata.getContentType());//Sets the MIME type of the response (e.g., video/mp4, audio/mpeg, image/png).
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
                /*
                adds a Content-Disposition header.
                "inline" means the file should be displayed/played directly in the browser (not forced as a download).
                 filename="..." suggests the name of the file if the user saves it.
                 */
                response.setHeader("Accept-Ranges", "bytes");
                /*
                Tells the client that the server supports byte-range requests.
                 This is important for video/audio players and download managers,
                  because it allows them to request specific portions of the file (seek/resume).
                 */
                response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(end - start + 1));
                //Sets the Content-Length header to the number of bytes being sent.
                IOUtils.copy(inputStream, response.getOutputStream());//copies the file data from the input stream into http response output stream
                response.flushBuffer();//ensures that all the streaming data is sent to the client immediately
            }
        } catch (Exception e) {
            logger.error("Failed to stream file {}", filename, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }



}
