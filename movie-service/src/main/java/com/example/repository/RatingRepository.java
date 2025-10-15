package com.example.repository;

import com.example.domain.entity.Movie;
import com.example.domain.entity.Rating;
import com.example.domain.enums.RatingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends BaseRepository<Rating, Long> {
    Page<Rating> findByMovie_IdAndStatus(Long movieId, RatingStatus status, Pageable pageable);
    boolean existsByMovieAndUserIdAndStatusNot(Movie movie, Long userId, RatingStatus status);
}
