package com.example.service.impl;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.enums.SupportChatStatus;
import com.example.domain.request.SupportChatAssignReqDTO;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.repository.SupportChatSessionRepository;
import com.example.service.SupportChatSessionService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SupportChatSessionServiceImpl
        extends BaseServiceImpl<SupportChatSession, Long, SupportChatSessionReqDTO, SupportChatSessionResDTO>
        implements SupportChatSessionService {

    private final SupportChatSessionRepository supportChatSessionRepository;
    private final SupportChatSessionMapper supportChatSessionMapper;

    protected SupportChatSessionServiceImpl(SupportChatSessionRepository supportChatSessionRepository,
                                            SupportChatSessionMapper supportChatSessionMapper) {
        super(supportChatSessionRepository);
        this.supportChatSessionRepository = supportChatSessionRepository;
        this.supportChatSessionMapper = supportChatSessionMapper;
    }

    @Override
    public SupportChatSessionResDTO findBySessionId(String sessionId) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiên chat"));
        return supportChatSessionMapper.toDto(session);
    }

    @Override
    public SupportChatSessionResDTO assignAgent(String sessionId, SupportChatAssignReqDTO dto) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));
        if (session.getStatus() != SupportChatStatus.OPEN)
            throw new IdInvalidException("Phiên chat đã được xử lý hoặc đóng.");

        session.setAgentId(dto.getAgentId());
        session.setStatus(SupportChatStatus.ASSIGNED);
        return supportChatSessionMapper.toDto(supportChatSessionRepository.save(session));
    }

    @Override
    public SupportChatSessionResDTO closeSession(String sessionId) throws IdInvalidException {
        SupportChatSession session = supportChatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IdInvalidException("Phiên chat không tồn tại"));

        session.setStatus(SupportChatStatus.CLOSED);
        return supportChatSessionMapper.toDto(supportChatSessionRepository.save(session));
    }

    @Override
    public SupportChatSessionResDTO createSession(SupportChatSessionReqDTO dto) {
        // Nếu sessionId không có => tự tạo UUID
        String sessionId = dto.getSessionId() != null ? dto.getSessionId() : UUID.randomUUID().toString();

        // Nếu user đã có session OPEN thì trả về session cũ
        Optional<SupportChatSession> existing = supportChatSessionRepository
                .findByUserIdAndStatus(dto.getUserId(), SupportChatStatus.OPEN);
        if (existing.isPresent())
            return supportChatSessionMapper.toDto(existing.get());

        SupportChatSession session = new SupportChatSession();
        session.setSessionId(sessionId);
        session.setUserId(dto.getUserId());
        session.setStatus(SupportChatStatus.OPEN);
        supportChatSessionRepository.save(session);
        return supportChatSessionMapper.toDto(session);
    }
}
