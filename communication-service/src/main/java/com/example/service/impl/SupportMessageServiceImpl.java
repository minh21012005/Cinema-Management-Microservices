package com.example.service.impl;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.entity.SupportMessage;
import com.example.domain.enums.SupportChatStatus;
import com.example.domain.enums.SupportMessageSender;
import com.example.domain.request.SupportMessageReadReqDTO;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportMessageMapper;
import com.example.repository.SupportChatSessionRepository;
import com.example.repository.SupportMessageRepository;
import com.example.service.SupportChatSessionService;
import com.example.service.SupportMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportMessageServiceImpl
        extends BaseServiceImpl<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO>
        implements SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;
    private final SupportChatSessionService supportChatSessionService;
    private final SupportChatSessionRepository supportChatSessionRepository;
    private final SupportMessageMapper supportMessageMapper;

    protected SupportMessageServiceImpl(SupportMessageRepository supportMessageRepository,
                                        SupportChatSessionService supportChatSessionService,
                                        SupportChatSessionRepository supportChatSessionRepository,
                                        SupportMessageMapper supportMessageMapper) {
        super(supportMessageRepository);
        this.supportMessageRepository = supportMessageRepository;
        this.supportChatSessionService = supportChatSessionService;
        this.supportChatSessionRepository = supportChatSessionRepository;
        this.supportMessageMapper = supportMessageMapper;
    }

    @Override
    public SupportMessageResDTO sendUserMessage(SupportMessageReqDTO dto) throws IdInvalidException {
        SupportChatSession session = supportChatSessionService.getSession();

        SupportMessage message = new SupportMessage();
        message.setSession(session);
        message.setSender(SupportMessageSender.USER);
        message.setContent(dto.getContent());
        supportMessageRepository.save(message);

        return supportMessageMapper.toDto(message);
    }

    @Override
    public SupportMessageResDTO sendAgentMessage(SupportMessageReqDTO dto) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(dto.getSessionId())
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long agentId = Long.valueOf(authentication.getName());

        if (!agentId.equals(session.getAgentId()))
            throw new IdInvalidException("Agent không có quyền gửi tin nhắn cho phiên này.");

        SupportMessage message = new SupportMessage();
        message.setSession(session);
        message.setSender(SupportMessageSender.AGENT);
        message.setContent(dto.getContent());
        supportMessageRepository.save(message);

        return supportMessageMapper.toDto(message);
    }

    @Override
    public void markAsRead(SupportMessageReadReqDTO dto) throws IdInvalidException {
        SupportMessage message = supportMessageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy tin nhắn."));
        message.setReadAt(LocalDateTime.now());
        supportMessageRepository.save(message);
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

        List<SupportMessage> list = supportMessageRepository.findBySession_UserIdOrderByCreatedAtAsc(userId);
        return list.stream().map(supportMessageMapper::toDto).collect(Collectors.toList());
    }
}
