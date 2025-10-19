package com.example.service.impl;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;
import com.example.mapper.ChatSessionMapper;
import com.example.repository.ChatSessionRepository;
import com.example.service.ChatSessionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatSessionServiceImpl
        extends BaseServiceImpl<ChatSession, Long, ChatSessionReqDTO, ChatSessionResDTO>
        implements ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatSessionMapper chatSessionMapper;

    protected ChatSessionServiceImpl(ChatSessionRepository chatSessionRepository,
                                     ChatSessionMapper chatSessionMapper) {
        super(chatSessionRepository);
        this.chatSessionRepository = chatSessionRepository;
        this.chatSessionMapper = chatSessionMapper;
    }

    @Override
    public ChatSession findOrCreateSession(ChatMessageReqDTO req) {
        ChatSession session = null;

        // Lấy thông tin user từ Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                userId = Long.valueOf(authentication.getName());
            } catch (NumberFormatException ignored) {
            }
        }

        // Nếu user đã login → lấy session active theo userId
        if (userId != null) {
            session = chatSessionRepository.findByUserIdAndActiveTrue(userId).orElse(null);
            if (session != null) return session;
        }

        // Nếu chưa login (guest) → lấy session theo sessionId từ request
        if (req.getSessionId() != null) {
            session = chatSessionRepository.findBySessionIdAndActiveTrue(req.getSessionId()).orElse(null);

            // Nếu guest login sau đó → gán userId
            if (session != null && userId != null && session.getUserId() == null) {
                session.setUserId(userId);
                chatSessionRepository.save(session);
            }

            if (session != null) return session;
        }

        // Nếu không có session → tạo mới
        return createSession(UUID.randomUUID().toString());
    }

    @Override
    @Transactional
    public ChatSessionResDTO resetSessionForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Bạn cần đăng nhập để reset session");
        }

        Long userId;
        try {
            userId = Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Không thể xác định userId hợp lệ");
        }

        // Vô hiệu hóa tất cả session cũ của user
        List<ChatSession> activeSessions = chatSessionRepository.findAll()
                .stream()
                .filter(s -> userId.equals(s.getUserId()) && s.isActive())
                .toList();

        for (ChatSession s : activeSessions) {
            s.setActive(false);
        }
        chatSessionRepository.saveAll(activeSessions);
        chatSessionRepository.flush();

        // Tạo session mới
        ChatSession newSession = new ChatSession();
        newSession.setSessionId(UUID.randomUUID().toString());
        newSession.setUserId(userId);
        newSession.setActive(true);
        chatSessionRepository.save(newSession);

        return chatSessionMapper.toDto(newSession);
    }

    @Override
    public ChatSession createSession(String sessionId) {
        ChatSession s = new ChatSession();
        s.setSessionId(sessionId);
        s.setActive(true);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                Long userId = Long.valueOf(authentication.getName());
                s.setUserId(userId);
            } catch (NumberFormatException ignored) {
            }
        }

        chatSessionRepository.save(s);
        return s;
    }

    @Scheduled(cron = "0 0 * * * *") // mỗi giờ
    @Transactional
    public void cleanupInactiveSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<ChatSession> oldSessions = chatSessionRepository.findOldGuestSessions(threshold);
        for (ChatSession s : oldSessions) {
            s.setActive(false);
        }
        chatSessionRepository.saveAll(oldSessions);
    }

    @Scheduled(cron = "0 0 0 * * *") // mỗi ngày
    @Transactional
    public void cleanupOldUserSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        List<ChatSession> oldSessions = chatSessionRepository.findOldUserSessions(threshold);
        for (ChatSession s : oldSessions) s.setActive(false);
        chatSessionRepository.saveAll(oldSessions);
    }
}
