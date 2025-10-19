package com.example.repository;

import com.example.domain.entity.FAQ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends BaseRepository<FAQ, Long> {

    List<FAQ> findByActiveTrue();

    @Query("""
            SELECT f FROM FAQ f
            WHERE f.active = true
              AND (
                LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.intent) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
           """)
    List<FAQ> searchByKeyword(@Param("keyword") String keyword);
}