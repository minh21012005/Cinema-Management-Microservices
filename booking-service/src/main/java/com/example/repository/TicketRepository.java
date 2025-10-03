package com.example.repository;

import com.example.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends BaseRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    @Query("""
        SELECT t FROM Ticket t
        WHERE t.showtimeId = :showtimeId
        AND (t.paid = true OR (t.reserved = true AND t.reservedAt >= :time))
    """)
    List<Ticket> findLockedSeats(
            @Param("showtimeId") Long showtimeId,
            @Param("time") LocalDateTime time
    );

    List<Ticket> findByReservedTrueAndReservedAtBefore(LocalDateTime time);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Ticket t " +
            "WHERE t.seatId = :seatId AND t.showtimeId = :showtimeId " +
            "AND (t.paid = true OR (t.reserved = true AND t.reservedAt > :expiredTime))")
    boolean existsBySeatIdAndShowtimeIdAndPaidTrueOrReservedTrue(
            @Param("seatId") Long seatId,
            @Param("showtimeId") Long showtimeId,
            @Param("expiredTime") LocalDateTime expiredTime);
}
