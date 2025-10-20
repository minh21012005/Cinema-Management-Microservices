package com.example.controller;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatAssignReqDTO;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.mapper.SupportChatSessionMapper;
import com.example.service.SupportChatSessionService;
import com.example.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{sessionId}/assign")
    public SupportChatSessionResDTO assignAgent(@PathVariable("sessionId") String sessionId,
                                                @RequestBody SupportChatAssignReqDTO dto) throws IdInvalidException {
        return supportChatSessionService.assignAgent(sessionId, dto);
    }

    @PostMapping("/{sessionId}/close")
    public SupportChatSessionResDTO closeSession(@PathVariable String sessionId) throws IdInvalidException {
        return supportChatSessionService.closeSession(sessionId);
    }

    @GetMapping("/{sessionId}")
    public SupportChatSessionResDTO getSession(@PathVariable String sessionId) throws IdInvalidException {
        return supportChatSessionService.findBySessionId(sessionId);
    }
}
