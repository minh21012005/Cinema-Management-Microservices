package com.example.controller;

import com.example.domain.entity.ChatMessage;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.mapper.ChatMessageMapper;
import com.example.service.ChatMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-messages")
public class ChatMessageController extends BaseController<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO> {

    private final ChatMessageService chatMessageService;

    protected ChatMessageController(ChatMessageService chatMessageService, ChatMessageMapper chatMessageMapper) {
        super(chatMessageService, chatMessageMapper);
        this.chatMessageService = chatMessageService;
    }


    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessageResDTO>> getChatHistoryForAnonymous(
            @PathVariable("sessionId") String sessionId) throws IdInvalidException {
        return ResponseEntity.ok(chatMessageService.getChatHistory(sessionId));
    }

    @GetMapping("/history/users/{userId}")
    public ResponseEntity<List<ChatMessageResDTO>> getChatHistoryForUser(
            @PathVariable("userId") Long userId) throws IdInvalidException {
        return ResponseEntity.ok(chatMessageService.getChatHistoryForUser(userId));
    }
}
