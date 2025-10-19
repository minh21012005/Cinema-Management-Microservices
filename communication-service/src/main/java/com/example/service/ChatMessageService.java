package com.example.service;

import com.example.domain.entity.ChatMessage;
import com.example.domain.entity.ChatSession;
import com.example.domain.enums.MessageType;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface ChatMessageService extends BaseService<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO> {
    List<ChatMessageResDTO> getChatHistory(String sessionId) throws IdInvalidException;
    List<ChatMessageResDTO> getChatHistoryForUser(Long userId) throws IdInvalidException;
    ChatMessageResDTO saveBotMessage(ChatSession session, String content, MessageType type);
}
