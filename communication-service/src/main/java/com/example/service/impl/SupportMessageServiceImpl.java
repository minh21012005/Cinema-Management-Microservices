package com.example.service.impl;

import com.example.client.UserClient;
import com.example.domain.entity.SupportChatSession;
import com.example.domain.entity.SupportMessage;
import com.example.domain.enums.SupportChatStatus;
import com.example.domain.enums.SupportMessageSender;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.mapper.SupportMessageMapper;
import com.example.repository.SupportChatSessionRepository;
import com.example.repository.SupportMessageRepository;
import com.example.service.SupportChatSessionService;
import com.example.service.SupportMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupportMessageServiceImpl
        extends BaseServiceImpl<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO>
        implements SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;
    private final SupportChatSessionService supportChatSessionService;
    private final SupportChatSessionRepository supportChatSessionRepository;
    private final SupportMessageMapper supportMessageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final SupportChatSessionMapper supportChatSessionMapper;
    private final UserClient userClient;

    protected SupportMessageServiceImpl(SupportMessageRepository supportMessageRepository,
                                        SupportChatSessionService supportChatSessionService,
                                        SupportChatSessionRepository supportChatSessionRepository,
                                        SupportMessageMapper supportMessageMapper,
                                        SimpMessagingTemplate messagingTemplate,
                                        SupportChatSessionMapper supportChatSessionMapper,
                                        UserClient userClient) {
        super(supportMessageRepository);
        this.supportMessageRepository = supportMessageRepository;
        this.supportChatSessionService = supportChatSessionService;
        this.supportChatSessionRepository = supportChatSessionRepository;
        this.supportMessageMapper = supportMessageMapper;
        this.messagingTemplate = messagingTemplate;
        this.supportChatSessionMapper = supportChatSessionMapper;
        this.userClient = userClient;
    }

    @Override
    public SupportMessageResDTO sendUserMessage(SupportMessageReqDTO dto) throws IdInvalidException {
        SupportChatSession session = supportChatSessionService.getSession();

        SupportMessage message = new SupportMessage();
        message.setSession(session);
        message.setSender(SupportMessageSender.USER);
        message.setContent(dto.getContent());

        if (session.getMessages() == null)
            session.setMessages(new ArrayList<>());

        session.getMessages().add(message);
        supportChatSessionRepository.save(session);

        SupportMessageResDTO saved = supportMessageMapper.toDto(message);

        Map<Long, String> userMap = userClient.getNamesByIds(List.of(session.getUserId())).getData();

        SupportChatSessionResDTO chatSessionResDTO = supportChatSessionMapper.toDto(session);
        chatSessionResDTO.setCustomerName(userMap.get(session.getUserId()));
        chatSessionResDTO.setLastMessage(message.getContent());

        boolean isNewSession = session.getMessages().size() == 1;
        if (isNewSession) {
            messagingTemplate.convertAndSend("/topic/support-sessions", chatSessionResDTO);
        }

        messagingTemplate.convertAndSend(
                "/topic/user/support-messages/" + session.getSessionId(), saved);

        return saved;
    }

    @Override
    public SupportMessageResDTO sendAgentMessage(SupportMessageReqDTO dto) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(dto.getSessionId())
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        if(session.getStatus().equals(SupportChatStatus.CLOSED)){
            throw new IdInvalidException("Phiên chat đã kết thúc, không thể gửi tin nhắn!");

        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        if (!agentId.equals(session.getAgentId()))
            throw new IdInvalidException("Agent không có quyền gửi tin nhắn cho phiên này.");

        SupportMessage message = new SupportMessage();
        message.setSession(session);
        message.setSender(SupportMessageSender.AGENT);
        message.setContent(dto.getContent());

        SupportMessageResDTO saved = supportMessageMapper.toDto(supportMessageRepository.save(message));

        messagingTemplate.convertAndSend(
                "/topic/agent/support-messages/" + session.getSessionId(), saved);

        return saved;
    }

    @Override
    public void markUserAsRead() throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        SupportChatSession session = supportChatSessionRepository
                .findByUserIdAndStatus(userId, SupportChatStatus.ASSIGNED)
                .orElseThrow(() -> new IdInvalidException("Session không tồn tại!"));

        List<SupportMessage> unreadMessages = supportMessageRepository
                .findBySession_SessionIdAndSenderAndReadAtIsNull(session.getSessionId(), SupportMessageSender.AGENT);

        unreadMessages.forEach(msg -> msg.setReadAt(LocalDateTime.now()));
        supportMessageRepository.saveAll(unreadMessages);
    }

    @Override
    public void markAgentAsRead(String sessionId) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository
                .findBySessionId(sessionId).orElseThrow(
                        () -> new IdInvalidException("Session không tồn tại!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        if (!agentId.equals(session.getAgentId())) {
            throw new IdInvalidException("Agent không có truy cập phiên này.");
        }

        List<SupportMessage> unreadMessages = supportMessageRepository
                .findBySession_SessionIdAndSenderAndReadAtIsNull(sessionId, SupportMessageSender.USER);

        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> msg.setReadAt(LocalDateTime.now()));
            supportMessageRepository.saveAll(unreadMessages);
        }
    }

    @Override
    public List<SupportMessageResDTO> getMessagesBySession(String sessionId) throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        if (!agentId.equals(session.getAgentId()))
            throw new IdInvalidException("Agent không có quyền gửi tin nhắn cho phiên này.");

        List<SupportMessage> list = supportMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId);
        return list.stream().map(supportMessageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SupportMessageResDTO> getMessageHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        Optional<SupportChatSession> session = supportChatSessionRepository
                .findByUserIdAndStatusNot(userId, SupportChatStatus.CLOSED);

        if (session.isEmpty()) {
            return new ArrayList<>();
        }

        List<SupportMessage> list = supportMessageRepository
                .findBySession_SessionIdOrderByCreatedAtAsc(session.get().getSessionId());
        return list.stream().map(supportMessageMapper::toDto).collect(Collectors.toList());
    }
}
