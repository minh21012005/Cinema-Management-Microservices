package com.example.repository;

import com.example.domain.entity.FAQ;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqRepository extends BaseRepository<FAQ, Long> {
    Optional<FAQ> findByQuestionIgnoreCase(String question);
    List<FAQ> findByTagsContainingIgnoreCase(String keyword);
    Optional<FAQ> findByIntent(String intent);
    List<FAQ> findByActiveTrue();
}
