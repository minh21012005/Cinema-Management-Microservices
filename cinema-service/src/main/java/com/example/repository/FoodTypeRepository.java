package com.example.repository;

import com.example.domain.entity.FoodType;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTypeRepository extends BaseRepository<FoodType, Long> {
}
