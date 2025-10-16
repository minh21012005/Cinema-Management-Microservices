package com.example.repository;

import com.example.domain.entity.ChatMessage;
import com.example.domain.entity.ChatSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);
    List<ChatMessage> findBySessionId(Long sessionId);
}
