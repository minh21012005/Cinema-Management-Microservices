package com.example.controller;

import com.example.domain.entity.ChatMessage;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.mapper.ChatMessageMapper;
import com.example.service.ChatMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-messages")
public class ChatMessageController extends BaseController<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO> {

    private final ChatMessageService chatMessageService;

    protected ChatMessageController(ChatMessageService chatMessageService, ChatMessageMapper chatMessageMapper) {
        super(chatMessageService, chatMessageMapper);
        this.chatMessageService = chatMessageService;
    }
}
