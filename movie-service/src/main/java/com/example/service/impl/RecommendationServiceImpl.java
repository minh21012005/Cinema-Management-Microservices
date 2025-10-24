package com.example.service.impl;

import com.example.domain.entity.Category;
import com.example.domain.entity.Movie;
import com.example.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final EmbeddingModel embeddingModel; //call open ai
    private final Map<String, Double> idfCache = new HashMap<>(); // tf-idf

    @Override
    public String buildMovieContent(Movie movie) {
        String categories = movie.getCategories() != null
                ? movie.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.joining(", "))
                : "";

        String raw = String.format(
                "%s %s %s %s %s",
                movie.getTitle(),
                movie.getDescription(),
                movie.getDirector(),
                movie.getCast(),
                categories
        );

        // --- Tiền xử lý văn bản ---
        String processed = raw.toLowerCase();                     // về chữ thường
        processed = processed.replaceAll("[^a-z0-9\\s]", " ");   // bỏ ký tự đặc biệt
        processed = processed.replaceAll("\\s+", " ").trim();    // gộp nhiều khoảng trắng

        return processed;
    }

    @Override
    public String createMovieEmbedding(Movie movie) {
        String content = buildMovieContent(movie);
        float[] embedding = embeddingModel.embed(content); // <-- đây là API đúng
        return Base64.getEncoder().encodeToString(floatArrayToByteArray(embedding));
    }

    /**
     * Tạo TF-IDF vector cho 1 phim
     */
    @Override
    public Map<String, Double> createMovieEmbeddingTfIdf(Movie movie, List<Movie> allMovies) {
        String[] words = buildMovieContent(movie)
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+");

        Map<String, Double> tf = new HashMap<>();
        for (String w : words) tf.put(w, tf.getOrDefault(w, 0.0) + 1.0);
        for (String w : tf.keySet()) tf.put(w, tf.get(w) / words.length);

        buildIdfCache(allMovies);

        Map<String, Double> tfidf = new HashMap<>();
        for (String w : tf.keySet()) {
            tfidf.put(w, tf.get(w) * idfCache.getOrDefault(w, 1.0));
        }
        return tfidf;
    }

    private void buildIdfCache(List<Movie> allMovies) {
        if (!idfCache.isEmpty()) return; // chỉ tính 1 lần
        Map<String, Long> dfMap = new HashMap<>();
        for (Movie m : allMovies) {
            Set<String> words = Arrays.stream(buildMovieContent(m)
                            .toLowerCase().replaceAll("[^a-z0-9 ]", " ").split("\\s+"))
                    .collect(Collectors.toSet());
            for (String w : words) dfMap.put(w, dfMap.getOrDefault(w, 0L) + 1);
        }
        for (Map.Entry<String, Long> e : dfMap.entrySet()) {
            double idf = Math.log((allMovies.size() + 1.0) / (e.getValue() + 1.0)) + 1.0;
            idfCache.put(e.getKey(), idf);
        }
    }

    private byte[] floatArrayToByteArray(float[] floats) {
        byte[] bytes = new byte[floats.length * 4];
        for (int i = 0; i < floats.length; i++) {
            int intBits = Float.floatToIntBits(floats[i]);
            bytes[i * 4] = (byte) ((intBits >> 24) & 0xFF);
            bytes[i * 4 + 1] = (byte) ((intBits >> 16) & 0xFF);
            bytes[i * 4 + 2] = (byte) ((intBits >> 8) & 0xFF);
            bytes[i * 4 + 3] = (byte) (intBits & 0xFF);
        }
        return bytes;
    }

    @Override
    public float cosineSimilarity(float[] vecA, float[] vecB) {
        float dot = 0f;
        float normA = 0f;
        float normB = 0f;
        for (int i = 0; i < vecA.length; i++) {
            dot += vecA[i] * vecB[i];
            normA += vecA[i] * vecA[i];
            normB += vecB[i] * vecB[i];
        }
        return dot / ((float) Math.sqrt(normA) * (float) Math.sqrt(normB));
    }

    /**
     * Cosine similarity cho 2 vector TF-IDF
     */
    @Override
    public double cosineSimilarity(Map<String, Double> vecA, Map<String, Double> vecB) {
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(vecA.keySet());
        allKeys.addAll(vecB.keySet());

        double dot = 0, normA = 0, normB = 0;
        for (String k : allKeys) {
            double a = vecA.getOrDefault(k, 0.0);
            double b = vecB.getOrDefault(k, 0.0);
            dot += a * b;
            normA += a * a;
            normB += b * b;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10); // tránh chia 0
    }
}
