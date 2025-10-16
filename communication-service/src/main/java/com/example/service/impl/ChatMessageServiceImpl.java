package com.example.service.impl;

import com.example.domain.entity.ChatMessage;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.mapper.ChatMessageMapper;
import com.example.repository.ChatMessageRepository;
import com.example.service.ChatMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl
        extends BaseServiceImpl<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO>
        implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    protected ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                     ChatMessageMapper chatMessageMapper) {
        super(chatMessageRepository);
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public List<ChatMessageResDTO> findBySessionId(Long sessionId) {
        return chatMessageRepository.findBySessionId(sessionId).stream()
                .map(chatMessageMapper::toDto)
                .toList();
    }
}
