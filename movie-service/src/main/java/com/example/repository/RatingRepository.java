package com.example.repository;

import com.example.domain.entity.Movie;
import com.example.domain.entity.Rating;
import com.example.domain.enums.RatingStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends BaseRepository<Rating, Long> {
    List<Rating> findByMovie_IdAndStatus(Long movieId, RatingStatus status);
    boolean existsByMovieAndUserIdAndStatusNot(Movie movie, Long userId, RatingStatus status);
}
