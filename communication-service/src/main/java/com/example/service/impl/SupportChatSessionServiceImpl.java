package com.example.service.impl;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.repository.SupportChatSessionRepository;
import com.example.service.SupportChatSessionService;
import org.springframework.stereotype.Service;

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
}
