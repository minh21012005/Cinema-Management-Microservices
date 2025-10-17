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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatMessageMapper messageMapper;
    private final FaqRepository faqRepository;
    private final ChatClient chatClient;
    private final CinemaToolService cinemaToolService;

    @Override
    @Transactional
    public ChatMessageResDTO handleUserMessage(ChatMessageReqDTO req) {
        // 1️⃣ Tìm hoặc tạo session
        ChatSession session = findOrCreateSession(req);

        // 2️⃣ Lưu tin nhắn user
        ChatMessage userMsg = messageMapper.toEntity(req);
        userMsg.setSession(session);
        userMsg.setSender(MessageSender.USER);
        userMsg.setType(MessageType.AI_GENERATED);
        messageRepository.save(userMsg);

        // 3️⃣ Kiểm tra FAQ trùng khớp
        var optFaq = faqRepository.findByQuestionIgnoreCase(req.getContent());
        if (optFaq.isPresent() && optFaq.get().isActive()) {
            return saveBotMessage(session, optFaq.get().getAnswer(), MessageType.FAQ_STATIC);
        }

        // 4️⃣ Build lịch sử hội thoại (context)
        List<ChatMessage> messages = messageRepository.findBySessionOrderByCreatedAtAsc(session);

        // 5️⃣ Tạo system prompt
        String systemPrompt = """
                Bạn là trợ lý ảo của rạp chiếu phim CNM Cinemas.
                Mục tiêu của bạn là hỗ trợ khách hàng một cách thân thiện, tự nhiên và chuyên nghiệp.
                
                Ngữ cảnh:
                - Bạn có quyền truy cập vào toàn bộ nội dung hội thoại trước đó, vì vậy hãy duy trì sự mạch lạc và nhất quán trong câu trả lời.
                - Nếu người dùng hỏi lại hoặc nhắc đến thông tin cũ, hãy dựa vào lịch sử hội thoại để phản hồi phù hợp.
                - Nếu câu hỏi yêu cầu dữ liệu thời gian thực (ví dụ: lịch chiếu, vé, thanh toán...), hãy sử dụng công cụ CinemaToolService để lấy dữ liệu thay vì tự suy luận.
                
                Phong cách phản hồi:
                - Trả lời ngắn gọn, rõ ràng, dễ hiểu.
                - Giữ giọng điệu lịch sự, gần gũi như một nhân viên chăm sóc khách hàng thật.
                - Không nhắc lại toàn bộ câu hỏi của người dùng trừ khi cần thiết để làm rõ ngữ cảnh.
                """;

        // 6️⃣ Xây ngữ cảnh hội thoại
        StringBuilder contextBuilder = new StringBuilder();
        for (ChatMessage msg : messages) {
            contextBuilder.append(msg.getSender() == MessageSender.USER ? "Người dùng: " : "Chatbot: ")
                    .append(msg.getContent())
                    .append("\n");
        }

        // 7️⃣ Gọi Spring AI (bản 1.0.3 chỉ có .user)
        var response = chatClient.prompt()
                .tools(cinemaToolService) // 👈 Cho phép model gọi các hàm @Tool
                .system(systemPrompt + "\n\nDưới đây là hội thoại trước đó:\n" + contextBuilder)
                .user(req.getContent())
                .call();

        String botReply = response.content();

        // 8️⃣ Lưu phản hồi
        return saveBotMessage(session,
                botReply != null ? botReply : "Xin lỗi, tôi hiện không trả lời được.",
                MessageType.AI_GENERATED);

    }

    private ChatMessageResDTO saveBotMessage(ChatSession session, String content, MessageType type) {
        ChatMessage botMsg = new ChatMessage();
        botMsg.setSession(session);
        botMsg.setSender(MessageSender.BOT);
        botMsg.setType(type);
        botMsg.setContent(content);
        messageRepository.save(botMsg);
        return messageMapper.toDto(botMsg);
    }

    private ChatSession findOrCreateSession(ChatMessageReqDTO req) {
        ChatSession session;

        if (req.getSessionId() != null) {
            session = sessionRepository.findBySessionId(req.getSessionId()).orElse(null);
            if (session != null) {
                // Nếu người dùng hiện tại đã đăng nhập mà session chưa có userId thì cập nhật
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken)) {
                    try {
                        Long userId = Long.valueOf(authentication.getName());
                        if (session.getUserId() == null) {
                            session.setUserId(userId);
                            sessionRepository.save(session);
                        }
                    } catch (NumberFormatException ignored) {}
                }
                return session;
            }
        }

        // Nếu không có sessionId hoặc session không tồn tại → tạo mới
        return createSession(UUID.randomUUID().toString());
    }

    private ChatSession createSession(String sessionId) {
        ChatSession s = new ChatSession();
        s.setSessionId(sessionId);
        s.setActive(true);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                Long userId = Long.valueOf(authentication.getName());
                s.setUserId(userId);
            } catch (NumberFormatException ignored) {}
        }

        sessionRepository.save(s);
        return s;
    }
}
