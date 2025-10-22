package com.example.repository;

import com.example.domain.entity.SupportMessage;
import com.example.domain.enums.SupportMessageSender;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportMessageRepository extends BaseRepository<SupportMessage, Long> {

    // Lấy danh sách tin nhắn theo sessionId, sắp xếp theo thời gian tạo tăng dần
    List<SupportMessage> findBySession_SessionIdOrderByCreatedAtAsc(String sessionId);

    List<SupportMessage> findBySession_UserIdOrderByCreatedAtAsc(Long userId);

    List<SupportMessage> findBySession_SessionIdAndSenderAndReadAtIsNull(String sessionId, SupportMessageSender sender);
}
