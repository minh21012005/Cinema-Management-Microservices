package com.example.repository;

import com.example.domain.entity.ChatSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends BaseRepository<ChatSession, Long> {

    Optional<ChatSession> findBySessionIdAndActiveTrue(String sessionId);

    Optional<ChatSession> findByUserIdAndActiveTrue(Long userId);

    @Query("SELECT s FROM ChatSession s WHERE s.userId IS NULL AND s.active = true AND s.updatedAt < :threshold")
    List<ChatSession> findOldGuestSessions(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT s FROM ChatSession s WHERE s.userId IS NOT NULL AND s.active = true AND s.updatedAt < :threshold")
    List<ChatSession> findOldUserSessions(@Param("threshold") LocalDateTime threshold);
}
