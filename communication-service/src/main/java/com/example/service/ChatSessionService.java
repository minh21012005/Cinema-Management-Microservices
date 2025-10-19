package com.example.service;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;

public interface ChatSessionService extends BaseService<ChatSession, Long, ChatSessionReqDTO, ChatSessionResDTO> {
    ChatSession findOrCreateSession(ChatMessageReqDTO req);
    ChatSession createSession(String sessionId);
    ChatSessionResDTO resetSessionForCurrentUser();
}
