package com.example.service.impl;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;
import com.example.mapper.ChatSessionMapper;
import com.example.repository.ChatSessionRepository;
import com.example.service.ChatSessionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatSessionServiceImpl
        extends BaseServiceImpl<ChatSession, Long, ChatSessionReqDTO, ChatSessionResDTO>
        implements ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatSessionMapper chatSessionMapper;

    protected ChatSessionServiceImpl(ChatSessionRepository chatSessionRepository,
                                     ChatSessionMapper chatSessionMapper) {
        super(chatSessionRepository);
        this.chatSessionRepository = chatSessionRepository;
        this.chatSessionMapper = chatSessionMapper;
    }

    @Override
    public List<ChatSessionResDTO> findByUserId(Long userId) {
        return chatSessionRepository.findByUserId(userId).stream()
                .map(chatSessionMapper::toDto)
                .toList();
    }
}
