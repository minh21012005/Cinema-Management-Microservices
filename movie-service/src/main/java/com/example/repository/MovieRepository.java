package com.example.repository;

import com.example.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findByTitleContainingIgnoreCase(@Param("title") String title);
    List<Movie> findByActiveTrue();
    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);
}
