package com.example.controller;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.service.SupportChatSessionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/support-chat-sessions")
public class SupportChatSessionController extends
        BaseController<SupportChatSession, Long, SupportChatSessionReqDTO, SupportChatSessionResDTO> {

    private final SupportChatSessionService supportChatSessionService;

    protected SupportChatSessionController(
            SupportChatSessionService supportChatSessionService,
            SupportChatSessionMapper supportChatSessionMapper) {
        super(supportChatSessionService, supportChatSessionMapper);
        this.supportChatSessionService = supportChatSessionService;
    }
}
