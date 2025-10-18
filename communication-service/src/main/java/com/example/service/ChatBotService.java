package com.example.service;

import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface ChatBotService {
    /**
     * Handle an incoming chat message: persist, process, return bot response DTO.
     */
    ChatMessageResDTO handleUserMessage(ChatMessageReqDTO req);
    List<ChatMessageResDTO> getChatHistory(String sessionId) throws IdInvalidException;
    List<ChatMessageResDTO> getChatHistoryForUser(Long userId) throws IdInvalidException;
}
