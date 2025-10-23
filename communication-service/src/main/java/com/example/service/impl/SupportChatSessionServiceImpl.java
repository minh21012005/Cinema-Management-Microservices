package com.example.service.impl;

import com.example.client.UserClient;
import com.example.domain.entity.SupportChatSession;
import com.example.domain.entity.SupportMessage;
import com.example.domain.enums.SupportChatStatus;
import com.example.domain.enums.SupportMessageSender;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.repository.SupportChatSessionRepository;
import com.example.repository.SupportMessageRepository;
import com.example.service.SupportChatSessionService;
import com.example.util.error.IdInvalidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupportChatSessionServiceImpl
        extends BaseServiceImpl<SupportChatSession, Long, SupportChatSessionReqDTO, SupportChatSessionResDTO>
        implements SupportChatSessionService {

    private final SupportChatSessionRepository supportChatSessionRepository;
    private final SupportMessageRepository supportMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SupportChatSessionMapper supportChatSessionMapper;
    private final UserClient userClient;

    protected SupportChatSessionServiceImpl(SupportChatSessionRepository supportChatSessionRepository,
                                            SupportMessageRepository supportMessageRepository,
                                            SimpMessagingTemplate messagingTemplate,
                                            SupportChatSessionMapper supportChatSessionMapper,
                                            UserClient userClient) {
        super(supportChatSessionRepository);
        this.supportChatSessionRepository = supportChatSessionRepository;
        this.supportMessageRepository = supportMessageRepository;
        this.messagingTemplate = messagingTemplate;
        this.supportChatSessionMapper = supportChatSessionMapper;
        this.userClient = userClient;
    }

    @Override
    public SupportChatSessionResDTO findBySessionId(String sessionId) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiên chat"));
        return supportChatSessionMapper.toDto(session);
    }

    @Override
    public SupportChatSessionResDTO assignAgent(String sessionId) throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        if (session.getStatus() != SupportChatStatus.OPEN)
            throw new IdInvalidException("Phiên chat đã được xử lý hoặc đóng.");

        session.setAgentId(agentId);
        session.setStatus(SupportChatStatus.ASSIGNED);

        SupportChatSession saved = supportChatSessionRepository.save(session);
        SupportChatSessionResDTO dto = supportChatSessionMapper.toDto(saved);

        // 🟢 Gửi realtime thông báo tới tất cả agent
        messagingTemplate.convertAndSend("/topic/support-session-updates", dto);

        return dto;
    }

    @Override
    public SupportChatSessionResDTO agentCloseSession(String sessionId) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        if (!agentId.equals(session.getAgentId())) {
            throw new IdInvalidException("Agent không có quyền đóng phiên chat này!");
        }

        session.setStatus(SupportChatStatus.CLOSED);
        SupportChatSession saved = supportChatSessionRepository.save(session);
        SupportChatSessionResDTO resDTO = supportChatSessionMapper.toDto(saved);

        messagingTemplate.convertAndSend(
                "/topic/agent/close/support-sessions/" + session.getSessionId(), resDTO);

        return resDTO;
    }

    @Override
    public SupportChatSessionResDTO userCloseSession() throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        SupportChatSession session = supportChatSessionRepository
                .findByUserIdAndStatusNot(userId, SupportChatStatus.CLOSED)
                .orElseThrow(() -> new IdInvalidException("Không còn phiên chat nào chưa đóng!"));

        session.setStatus(SupportChatStatus.CLOSED);
        SupportChatSession saved = supportChatSessionRepository.save(session);
        SupportChatSessionResDTO resDTO = supportChatSessionMapper.toDto(saved);

        messagingTemplate.convertAndSend(
                "/topic/user/close/support-sessions/" + session.getSessionId(), resDTO);

        return resDTO;
    }

    @Override
    public SupportChatSession getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        // Nếu user đã có session OPEN/ASSIGNED thì trả về session cũ
        Optional<SupportChatSession> existing = supportChatSessionRepository
                .findByUserIdAndStatusNot(userId, SupportChatStatus.CLOSED);
        SupportChatSession session;

        if (existing.isPresent()) {
            session = existing.get();
        } else {
            String sessionId = UUID.randomUUID().toString();

            session = new SupportChatSession();
            session.setSessionId(sessionId);
            session.setUserId(userId);
            session.setStatus(SupportChatStatus.OPEN);
            session = supportChatSessionRepository.save(session);
        }

        return session;
    }

    @Override
    public List<SupportChatSessionResDTO> getSessionsByStatus(SupportChatStatus status) {
        List<SupportChatSession> sessions;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        if (status == null) {
            sessions = supportChatSessionRepository.findAll();
        } else if (status == SupportChatStatus.ASSIGNED || status == SupportChatStatus.CLOSED) {
            sessions = supportChatSessionRepository.findByAgentIdAndStatus(agentId, status);
        } else {
            sessions = supportChatSessionRepository.findByStatus(status);
        }

        List<Long> userIds = sessions.stream()
                .map(SupportChatSession::getUserId)
                .distinct()
                .toList();

        Map<Long, String> userMap = userClient.getNamesByIds(userIds).getData();

        // gán username
        return sessions.stream().map(s -> {
            SupportChatSessionResDTO dto = supportChatSessionMapper.toDto(s);
            dto.setCustomerName(userMap.get(s.getUserId())); // gán username
            dto.setLastMessage(s.getMessages().getLast().getContent());

            List<SupportMessage> messageList = supportMessageRepository
                    .findBySession_SessionIdAndSenderAndReadAtIsNull(s.getSessionId(), SupportMessageSender.USER);

            dto.setUnreadCountForAgent(messageList.size());

            return dto;
        }).toList();
    }

}
