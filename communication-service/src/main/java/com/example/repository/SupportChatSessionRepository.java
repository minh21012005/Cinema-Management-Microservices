package com.example.repository;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.enums.SupportChatStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportChatSessionRepository extends BaseRepository<SupportChatSession, Long> {

    // Tìm session theo sessionId
    Optional<SupportChatSession> findBySessionId(String sessionId);

    // Tìm session đang mở theo userId
    Optional<SupportChatSession> findByUserIdAndStatus(Long userId, SupportChatStatus status);
}
