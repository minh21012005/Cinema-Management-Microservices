package com.example.controller;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportMessageMapper;
import com.example.service.SupportMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SupportMessageResDTO> sendUserMessage(@RequestBody SupportMessageReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(supportMessageService.sendUserMessage(dto));
    }

    @PostMapping("/agent")
    public ResponseEntity<SupportMessageResDTO> sendAgentMessage(
            @RequestBody SupportMessageReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(supportMessageService.sendAgentMessage(dto));
    }

    @PostMapping("/user/read")
    public void markUserAsRead() throws IdInvalidException {
        supportMessageService.markUserAsRead();
    }

    @PostMapping("/agent/read")
    public void markAgentAsRead() throws IdInvalidException {
        supportMessageService.markAgentAsRead();
    }

    @GetMapping("/session")
    public ResponseEntity<List<SupportMessageResDTO>> getMessageHistory(){
        return ResponseEntity.ok(supportMessageService.getMessageHistory());
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<SupportMessageResDTO>> getMessagesBySession
            (@PathVariable("sessionId") String sessionId) throws IdInvalidException {
        return ResponseEntity.ok(supportMessageService.getMessagesBySession(sessionId));
    }
}

