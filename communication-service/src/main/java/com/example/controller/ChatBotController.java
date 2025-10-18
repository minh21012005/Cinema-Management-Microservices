package com.example.controller;

import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.service.ChatBotService;
import com.example.util.error.IdInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-bot")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<ChatMessageResDTO> chat(@RequestBody ChatMessageReqDTO req) {
        ChatMessageResDTO reply = chatBotService.handleUserMessage(req);
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessageResDTO>> getChatHistoryForAnonymous(
            @PathVariable("sessionId") String sessionId) throws IdInvalidException {
        return ResponseEntity.ok(chatBotService.getChatHistory(sessionId));
    }

    @GetMapping("/history/users/{userId}")
    public ResponseEntity<List<ChatMessageResDTO>> getChatHistoryForUser(
            @PathVariable("userId") Long userId) throws IdInvalidException {
        return ResponseEntity.ok(chatBotService.getChatHistoryForUser(userId));
    }
}
