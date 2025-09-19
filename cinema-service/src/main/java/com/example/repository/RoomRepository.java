package com.example.repository;

import com.example.domain.entity.Cinema;
import com.example.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends BaseRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findByCinema_Id(Long cinemaId);
    boolean existsByNameAndCinema(String name, Cinema cinema);
}
