package com.example.controller;

import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
