package com.example.controller;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;
import com.example.mapper.ChatSessionMapper;
import com.example.service.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-sessions")
public class ChatSessionController extends BaseController<ChatSession, Long, ChatSessionReqDTO, ChatSessionResDTO> {

    private final ChatSessionService chatSessionService;

    protected ChatSessionController(ChatSessionService chatSessionService, ChatSessionMapper chatSessionMapper) {
        super(chatSessionService, chatSessionMapper);
        this.chatSessionService = chatSessionService;
    }

    @PostMapping("/reset")
    public ResponseEntity<ChatSessionResDTO> resetSession() {
        return ResponseEntity.ok(chatSessionService.resetSessionForCurrentUser());
    }
}
