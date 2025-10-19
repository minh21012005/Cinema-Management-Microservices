package com.example.service.impl;

import com.example.domain.entity.ChatMessage;
import com.example.domain.entity.ChatSession;
import com.example.domain.enums.MessageSender;
import com.example.domain.enums.MessageType;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.mapper.ChatMessageMapper;
import com.example.repository.ChatMessageRepository;
import com.example.repository.ChatSessionRepository;
import com.example.service.ChatMessageService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl
        extends BaseServiceImpl<ChatMessage, Long, ChatMessageReqDTO, ChatMessageResDTO>
        implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageMapper chatMessageMapper;

    protected ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                     ChatMessageMapper chatMessageMapper,
                                     ChatSessionRepository chatSessionRepository) {
        super(chatMessageRepository);
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
        this.chatSessionRepository = chatSessionRepository;
    }

    @Override
    public List<ChatMessageResDTO> getChatHistory(String sessionId) throws IdInvalidException {
        ChatSession chatSession = chatSessionRepository.findBySessionIdAndActiveTrue(sessionId).orElseThrow(
                () -> new IdInvalidException("Session không hợp lệ")
        );

        return chatSession.getMessages().stream().map(chatMessageMapper::toDto).toList();
    }

    @Override
    public List<ChatMessageResDTO> getChatHistoryForUser(Long userId) throws IdInvalidException {
        ChatSession chatSession = chatSessionRepository.findByUserIdAndActiveTrue(userId).orElseThrow(
                () -> new IdInvalidException("Session không hợp lệ")
        );

        return chatSession.getMessages().stream().map(chatMessageMapper::toDto).toList();
    }

    @Override
    public ChatMessageResDTO saveBotMessage(ChatSession session, String content, MessageType type) {
        ChatMessage botMsg = new ChatMessage();
        botMsg.setSession(session);
        botMsg.setSender(MessageSender.BOT);
        botMsg.setType(type);
        botMsg.setContent(content);
        chatMessageRepository.save(botMsg);
        return chatMessageMapper.toDto(botMsg);
    }
}
