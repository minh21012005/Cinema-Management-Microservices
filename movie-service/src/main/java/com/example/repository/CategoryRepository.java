package com.example.repository;

import com.example.domain.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {
    List<Category> findByActiveTrue();
}
