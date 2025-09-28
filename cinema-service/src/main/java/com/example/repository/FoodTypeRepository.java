package com.example.repository;

import com.example.domain.entity.FoodType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends BaseRepository<FoodType, Long> {
    List<FoodType> findByActiveTrue();
}
