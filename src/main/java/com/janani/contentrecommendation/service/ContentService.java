package com.janani.contentrecommendation.service;

import com.janani.contentrecommendation.dto.ContentRequestDTO;
import com.janani.contentrecommendation.dto.ContentResponseDTO;
import com.janani.contentrecommendation.entity.ContentType;

import java.util.List;

    public interface ContentService {

        ContentResponseDTO createContent(ContentRequestDTO contentRequestDTO);

        ContentResponseDTO updateContent(Long id, ContentRequestDTO contentRequestDTO);

        void deleteContent(Long id);

        ContentResponseDTO getContentById(Long id);

        List<ContentResponseDTO> getAllContents();

        List<ContentResponseDTO> getContentsByCategory(String category);

        List<ContentResponseDTO> getContentsByType(ContentType type);

        List<ContentResponseDTO> searchContentsByTitle(String keyword);
    }


