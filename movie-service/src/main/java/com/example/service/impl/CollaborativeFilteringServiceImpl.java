package com.example.service.impl;

import com.example.domain.entity.Rating;
import com.example.domain.response.MovieResDTO;
import com.example.mapper.MovieMapper;
import com.example.repository.MovieRepository;
import com.example.repository.RatingRepository;
import com.example.service.CollaborativeFilteringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CollaborativeFilteringServiceImpl implements CollaborativeFilteringService {
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public List<MovieResDTO> recommendByUserBased(Long userId, int topN) {
        // 1️⃣ Lấy toàn bộ rating
        List<Rating> ratings = ratingRepository.findAll();

        // 2️⃣ Chuyển sang map user → (movie → stars)
        Map<Long, Map<Long, Integer>> userRatings = new HashMap<>();
        for (Rating r : ratings) {
            userRatings
                    .computeIfAbsent(r.getUserId(), k -> new HashMap<>())
                    .put(r.getMovie().getId(), r.getStars());
        }

        // 3️⃣ Tính tương đồng giữa user hiện tại và các user khác
        Map<Long, Double> similarity = new HashMap<>();
        Map<Long, Integer> target = userRatings.get(userId);
        if (target == null || target.isEmpty()) return Collections.emptyList();

        for (Long otherId : userRatings.keySet()) {
            if (!otherId.equals(userId)) {
                double sim = cosineSimilarity(target, userRatings.get(otherId));
                if (sim > 0) similarity.put(otherId, sim);
            }
        }

        if (similarity.isEmpty()) return Collections.emptyList();

        // 4️⃣ Dự đoán điểm cho phim chưa xem
        Map<Long, Double> predictedRatings = new HashMap<>();
        Map<Long, Double> simSums = new HashMap<>();

        for (Map.Entry<Long, Double> simEntry : similarity.entrySet()) {
            Long otherId = simEntry.getKey();
            double sim = simEntry.getValue();

            for (Map.Entry<Long, Integer> entry : userRatings.get(otherId).entrySet()) {
                Long movieId = entry.getKey();
                int rating = entry.getValue();

                // Bỏ qua nếu user này đã xem phim
                if (target.containsKey(movieId)) continue;

                // Cộng dồn (similarity * rating)
                predictedRatings.merge(movieId, sim * rating, Double::sum);
                // Cộng dồn tổng similarity (để chuẩn hóa)
                simSums.merge(movieId, Math.abs(sim), Double::sum);
            }
        }

        // 5️⃣ Chuẩn hóa điểm từng phim
        for (Long movieId : predictedRatings.keySet()) {
            predictedRatings.put(movieId,
                    predictedRatings.get(movieId) / (simSums.get(movieId) + 1e-10));
        }

        // 6️⃣ Lấy top N phim
        return predictedRatings.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(topN)
                .map(e -> movieRepository.findById(e.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public List<MovieResDTO> recommendByItemBased(Long userId, int topN) {
        // 1️⃣ Lấy toàn bộ rating
        List<Rating> ratings = ratingRepository.findAll();

        // 2️⃣ Tạo map movie → (user → stars)
        Map<Long, Map<Long, Integer>> movieRatings = new HashMap<>();
        for (Rating r : ratings) {
            movieRatings
                    .computeIfAbsent(r.getMovie().getId(), k -> new HashMap<>())
                    .put(r.getUserId(), r.getStars());
        }

        // 3️⃣ Lấy các phim mà user hiện tại đã xem
        Map<Long, Integer> userRatedMovies = new HashMap<>();
        for (Rating r : ratings) {
            if (r.getUserId().equals(userId)) {
                userRatedMovies.put(r.getMovie().getId(), r.getStars());
            }
        }

        if (userRatedMovies.isEmpty()) return Collections.emptyList();

        // 4️⃣ Dự đoán điểm cho phim chưa xem
        Map<Long, Double> predictedRatings = new HashMap<>();
        Map<Long, Double> simSums = new HashMap<>();

        for (Map.Entry<Long, Integer> ratedEntry : userRatedMovies.entrySet()) {
            Long ratedMovieId = ratedEntry.getKey();
            int rating = ratedEntry.getValue();

            for (Long otherMovieId : movieRatings.keySet()) {
                if (otherMovieId.equals(ratedMovieId)) continue; // bỏ qua chính nó
                if (userRatedMovies.containsKey(otherMovieId)) continue; // user đã xem rồi

                // 5️⃣ Tính similarity giữa 2 phim
                double sim = cosineSimilarity(
                        movieRatings.get(ratedMovieId),
                        movieRatings.get(otherMovieId)
                );

                if (sim <= 0) continue; // chỉ xét phim tương tự dương

                // 6️⃣ Cộng dồn similarity * rating
                predictedRatings.merge(otherMovieId, sim * rating, Double::sum);
                simSums.merge(otherMovieId, Math.abs(sim), Double::sum);
            }
        }

        // 7️⃣ Chuẩn hóa điểm dự đoán
        for (Long movieId : predictedRatings.keySet()) {
            predictedRatings.put(movieId,
                    predictedRatings.get(movieId) / (simSums.get(movieId) + 1e-10));
        }

        // 8️⃣ Lấy top N phim
        return predictedRatings.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(topN)
                .map(e -> movieRepository.findById(e.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public double cosineSimilarity(Map<Long, Integer> a, Map<Long, Integer> b) {
        Set<Long> all = new HashSet<>();
        all.addAll(a.keySet());
        all.addAll(b.keySet());

        double dot = 0, na = 0, nb = 0;
        for (Long k : all) {
            double x = a.getOrDefault(k, 0);
            double y = b.getOrDefault(k, 0);
            dot += x * y;
            na += x * x;
            nb += y * y;
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb) + 1e-10);
    }
}