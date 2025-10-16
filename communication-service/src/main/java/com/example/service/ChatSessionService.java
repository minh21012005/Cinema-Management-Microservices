package com.example.service;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;

import java.util.List;

public interface ChatSessionService extends BaseService<ChatSession, Long, ChatSessionReqDTO, ChatSessionResDTO> {
    List<ChatSessionResDTO> findByUserId(Long userId);
}
