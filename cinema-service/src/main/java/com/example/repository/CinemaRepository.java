package com.example.repository;

import com.example.domain.entity.Cinema;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends BaseRepository<Cinema, Long>, JpaSpecificationExecutor<Cinema> {
    boolean existsByName(String name);

    boolean existsByAddress(String address);

    boolean existsByPhone(String phone);
}
