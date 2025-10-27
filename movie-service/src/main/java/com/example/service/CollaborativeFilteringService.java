package com.example.service;

import com.example.domain.response.MovieResDTO;

import java.util.List;
import java.util.Map;

public interface CollaborativeFilteringService {
    List<MovieResDTO> recommendByUserBased(Long userId, int topN);
    List<MovieResDTO> recommendByItemBased(Long userId, int topN);
    double cosineSimilarity(Map<Long, Integer> a, Map<Long, Integer> b);
}
