package com.example.service;

import com.example.domain.entity.ChatMessage;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;

import java.util.List;

public interface ChatMessageService extends BaseService<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO> {
    List<ChatMessageResDTO> findBySessionId(Long sessionId);
}
