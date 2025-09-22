package com.example.repository;

import com.example.domain.entity.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends BaseRepository<Movie, Long> {
}
