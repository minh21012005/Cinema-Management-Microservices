package com.example.repository;

import com.example.domain.entity.Movie;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT m FROM Movie m
        WHERE m.active = true
        AND m.releaseDate <= :today
        AND (m.endDate IS NULL OR m.endDate >= :today)
        ORDER BY m.releaseDate DESC
    """)
    List<Movie> findNowShowingMovies(@Param("today") LocalDate today, @Param("pageable") Pageable pageable);

    @Query("""
        SELECT m FROM Movie m
        WHERE m.active = true
        AND m.releaseDate > :today
        ORDER BY m.releaseDate ASC
    """)
    List<Movie> findComingSoonMovies(@Param("today") LocalDate today, @Param("pageable") Pageable pageable);
}
