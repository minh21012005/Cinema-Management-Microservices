package com.example.repository;

import com.example.domain.entity.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends BaseRepository<Seat, Long> {
    Optional<Seat> findByRowIndexAndColIndexAndRoomId(Integer rowIndex, Integer colIndex, Long roomId);

    @Query("SELECT SUM(CASE WHEN s.active = true THEN 1 ELSE 0 END) " +
            "FROM Seat s " +
            "JOIN s.room r " +
            "JOIN r.showtimes st " +
            "WHERE st.active = true " +
            "AND MONTH(st.startTime) = :month " +
            "AND YEAR(st.startTime) = :year")
    Long countActiveSeatsByMonth(@Param("year") int year, @Param("month") int month);
}
