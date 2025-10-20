package com.example.controller;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReadReqDTO;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportMessageMapper;
import com.example.service.SupportMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support-chat-messages")
public class SupportMessageController extends
        BaseController<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO> {

    private final SupportMessageService supportMessageService;

    protected SupportMessageController(
            SupportMessageService supportMessageService,
            SupportMessageMapper supportMessageMapper) {
        super(supportMessageService, supportMessageMapper);
        this.supportMessageService = supportMessageService;
    }

    @PostMapping("/user")
    public SupportMessageResDTO sendUserMessage(@RequestBody SupportMessageReqDTO dto) throws IdInvalidException {
        return supportMessageService.sendUserMessage(dto);
    }

    @PostMapping("/agent/{agentId}")
    public SupportMessageResDTO sendAgentMessage(
            @PathVariable("agentId") Long agentId,
            @RequestBody SupportMessageReqDTO dto) throws IdInvalidException {
        return supportMessageService.sendAgentMessage(agentId, dto);
    }

    @PostMapping("/read")
    public void markAsRead(@RequestBody SupportMessageReadReqDTO dto) throws IdInvalidException {
        supportMessageService.markAsRead(dto);
    }

    @GetMapping("/session/{sessionId}")
    public List<SupportMessageResDTO> getMessagesBySession(@PathVariable String sessionId) {
        return supportMessageService.getMessagesBySession(sessionId);
    }
}

