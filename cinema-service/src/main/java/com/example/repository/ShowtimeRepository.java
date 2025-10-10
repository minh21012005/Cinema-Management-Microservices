package com.example.repository;

import com.example.domain.entity.Showtime;
import com.example.domain.response.ShowtimeResDTO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends BaseRepository<Showtime, Long>, JpaSpecificationExecutor<Showtime> {
    @Query("SELECT s FROM Showtime s " +
            "WHERE s.room.id = :roomId " +
            "AND s.active = true " +
            "AND (:startTime < s.endTime AND :endTime > s.startTime)")
    List<Showtime> findOverlappingShowtimes(@Param("roomId") Long roomId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    @Query("""
                SELECT s FROM Showtime s
                WHERE s.room.id = :roomId
                  AND s.id <> :showtimeId
                  AND s.active = true
                  AND s.startTime < :endTime
                  AND :startTime < s.endTime
            """)
    List<Showtime> findOverlappingShowtimesExcept(
            @Param("roomId") Long roomId,
            @Param("showtimeId") Long showtimeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Showtime s WHERE s.room.cinema.id = :cinemaId")
    List<Showtime> findAllByCinemaId(@Param("cinemaId") Long cinemaId);

    List<Showtime> findByMovieIdAndActiveTrueAndStartTimeAfter(Long movieId, LocalDateTime now);

    @Query("""
                SELECT s FROM Showtime s
                JOIN s.room r
                JOIN r.cinema c
                WHERE s.movieId = :movieId
                  AND s.active = true
                  AND s.startTime BETWEEN :startDate AND :endDate
                  AND s.startTime > CURRENT_TIMESTAMP
                  AND (:cinemaId IS NULL OR c.id = :cinemaId)
                ORDER BY s.startTime ASC
            """)
    List<Showtime> findActiveShowtimesByMovieAndOptionalCinema(
            @Param("movieId") Long movieId,
            @Param("cinemaId") Long cinemaId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
