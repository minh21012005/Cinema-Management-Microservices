package com.example.repository;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.enums.SupportChatStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportChatSessionRepository extends BaseRepository<SupportChatSession, Long> {

    // Tìm session theo sessionId
    Optional<SupportChatSession> findBySessionId(String sessionId);

    Optional<SupportChatSession> findByUserIdAndStatusNot(Long userId, SupportChatStatus status);

    // Tìm session đang mở theo userId
    Optional<SupportChatSession> findByUserIdAndStatus(Long userId, SupportChatStatus status);

    List<SupportChatSession> findByStatus(SupportChatStatus status);

    List<SupportChatSession> findByAgentIdAndStatus(Long agentId, SupportChatStatus status);
}
