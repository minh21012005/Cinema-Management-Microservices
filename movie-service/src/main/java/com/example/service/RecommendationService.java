package com.example.service;

import com.example.domain.entity.Movie;

import java.util.List;
import java.util.Map;

public interface RecommendationService {
    String buildMovieContent(Movie movie);
    String createMovieEmbedding(Movie movie);
    float cosineSimilarity(float[] vecA, float[] vecB);
    Map<String, Double> createMovieEmbeddingTfIdf(Movie movie, List<Movie> allMovies);
    double cosineSimilarity(Map<String, Double> vecA, Map<String, Double> vecB);
}
