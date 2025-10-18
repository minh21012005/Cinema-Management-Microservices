package com.example.repository;

import com.example.domain.entity.ChatSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends BaseRepository<ChatSession, Long> {
    Optional<ChatSession> findBySessionId(String sessionId);
    Optional<ChatSession> findBySessionIdAndActiveTrue(String sessionId);
    Optional<ChatSession> findByUserIdAndActiveTrue(Long userId);
    List<ChatSession> findByUserId(Long userId);
}
