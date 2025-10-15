package com.example.repository;

import com.example.domain.entity.Rating;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends BaseRepository<Rating, Long> {
}
