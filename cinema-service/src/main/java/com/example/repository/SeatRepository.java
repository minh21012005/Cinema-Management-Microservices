package com.example.repository;

import com.example.domain.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends BaseRepository<Seat, Long> {
    Optional<Seat> findByRowIndexAndColIndexAndRoomId(Integer rowIndex, Integer colIndex, Long roomId);
}
