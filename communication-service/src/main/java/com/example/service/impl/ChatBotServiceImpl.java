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
import com.example.repository.FaqRepository;
import com.example.service.ChatBotService;
import com.example.service.internal.CinemaToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatMessageMapper messageMapper;
    private final FaqRepository faqRepository;
    private final ChatClient chatClient;
    private final CinemaToolService cinemaToolService; // tool bean

    @Override
    @Transactional
    public ChatMessageResDTO handleUserMessage(ChatMessageReqDTO req) {
        // 1. find or create session
        ChatSession session = findOrCreateSession(req);

        // 2. save user message
        ChatMessage userMessage = messageMapper.toEntity(req);
        userMessage.setSession(session);
        userMessage.setSender(MessageSender.USER);
        userMessage.setType(MessageType.AI_GENERATED);
        messageRepository.save(userMessage);

        // 3. quick FAQ exact match (optional)
        var optFaq = faqRepository.findByQuestionIgnoreCase(req.getContent());
        if (optFaq.isPresent() && optFaq.get().isActive()) {
            String faqAnswer = optFaq.get().getAnswer();
            ChatMessage botMsg = new ChatMessage();
            botMsg.setSession(session);
            botMsg.setSender(MessageSender.BOT);
            botMsg.setType(MessageType.FAQ_STATIC);
            botMsg.setContent(faqAnswer);
            messageRepository.save(botMsg);
            return messageMapper.toDto(botMsg);
        }

        // 4. call OpenAI via ChatClient with tools registered
        // build prompt with system instructions + sample FAQs
        String system = "Bạn là chatbot rạp CNM Cinemas. Nếu câu hỏi trùng FAQ, trả trực tiếp theo nội dung FAQ. Nếu cần dữ liệu thời gian thực, hãy gọi tool tương ứng.";
        var response = chatClient.prompt()
                .system(system)
                .user(req.getContent())
                .tools(cinemaToolService)
                .call();

        // 5. response.content() contains final textual reply (Spring AI calls tools automatically)
        String botReply = response.content();

        // 6. save bot message
        ChatMessage botMsg = new ChatMessage();
        botMsg.setSession(session);
        botMsg.setSender(MessageSender.BOT);
        // If model invoked tools, you may choose type INTERNAL_API; but Spring AI doesn't expose that in response directly.
        botMsg.setType(MessageType.AI_GENERATED);
        botMsg.setContent(botReply != null ? botReply : "Xin lỗi, tôi hiện không trả lời được.");
        messageRepository.save(botMsg);

        return messageMapper.toDto(botMsg);
    }

    private ChatSession findOrCreateSession(ChatMessageReqDTO req) {
        if (req.getSessionId() != null) {
            Optional<ChatSession> sOpt = sessionRepository.findBySessionId(req.getSessionId());
            if (sOpt.isPresent()) return sOpt.get();
        }
        ChatSession s = new ChatSession();
        s.setSessionId(req.getSessionId() == null ? UUID.randomUUID().toString() : req.getSessionId());
        s.setUserId(null);
        s.setActive(true);
        sessionRepository.save(s);
        return s;
    }
}
