package com.janani.contentrecommendation.service.impl;

import com.janani.contentrecommendation.dto.ContentRequestDTO;
import com.janani.contentrecommendation.dto.ContentResponseDTO;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.ContentType;

import com.janani.contentrecommendation.repository.ContentRepository;
import com.janani.contentrecommendation.service.ContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl  {
/*
    private final ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public ContentResponseDTO createContent(ContentRequestDTO contentRequestDTO) {
        Content content = ContentMapper.toEntity(contentRequestDTO);
        Content savedContent = contentRepository.save(content);
        return ContentMapper.toResponseDTO(savedContent);
    }

    @Override
    public ContentResponseDTO updateContent(Long id, ContentRequestDTO contentRequestDTO) {
        Content existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        existingContent.setTitle(contentRequestDTO.getTitle());
        existingContent.setDescription(contentRequestDTO.getDescription());
        existingContent.setType(Enum.valueOf(ContentType.class, contentRequestDTO.getType()));
        existingContent.setCategory(contentRequestDTO.getCategory());
        existingContent.setTags(contentRequestDTO.getTags());
        existingContent.setS3Url(contentRequestDTO.getS3Url());

        Content updatedContent = contentRepository.save(existingContent);
        return ContentMapper.toResponseDTO(updatedContent);
    }

    @Override
    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new RuntimeException("Content not found with id: " + id);
        }
        contentRepository.deleteById(id);
    }

    @Override
    public ContentResponseDTO getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));
        return ContentMapper.toResponseDTO(content);
    }

    @Override
    public List<ContentResponseDTO> getAllContents() {
        return contentRepository.findAll()
                .stream()
                .map(ContentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponseDTO> getContentsByCategory(String category) {
        return contentRepository.findByCategory(category)
                .stream()
                .map(ContentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponseDTO> getContentsByType(ContentType type) {
        return contentRepository.findByType(type)
                .stream()
                .map(ContentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponseDTO> searchContentsByTitle(String keyword) {
        return contentRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(ContentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
*/
}