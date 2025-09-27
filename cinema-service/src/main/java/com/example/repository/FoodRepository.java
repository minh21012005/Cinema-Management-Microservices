package com.example.repository;

import com.example.domain.entity.Food;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends BaseRepository<Food, Long>, JpaSpecificationExecutor<Food> {
    boolean existsByCode(String code);
}
