package com.example.service;

import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;

public interface ChatBotService {
    /**
     * Handle an incoming chat message: persist, process, return bot response DTO.
     */
    ChatMessageResDTO handleUserMessage(ChatMessageReqDTO req);
}
