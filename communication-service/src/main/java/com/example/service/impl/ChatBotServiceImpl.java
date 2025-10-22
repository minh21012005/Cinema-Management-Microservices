package com.example.service.impl;

import com.example.domain.entity.ChatMessage;
import com.example.domain.entity.ChatSession;
import com.example.domain.entity.FAQ;
import com.example.domain.enums.MessageSender;
import com.example.domain.enums.MessageType;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import com.example.mapper.ChatMessageMapper;
import com.example.repository.ChatMessageRepository;
import com.example.repository.FaqRepository;
import com.example.service.ChatBotService;
import com.example.service.ChatMessageService;
import com.example.service.ChatSessionService;
import com.example.service.internal.CinemaToolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatMessageRepository messageRepository;
    private final ChatSessionService chatSessionService;
    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper messageMapper;
    private final FaqRepository faqRepository;
    private final ChatClient chatClient;
    private final CinemaToolService cinemaToolService;

    @Override
    @Transactional
    public ChatMessageResDTO handleUserMessage(ChatMessageReqDTO req) {
        // 1️⃣ Tìm hoặc tạo session
        ChatSession session = chatSessionService.findOrCreateSession(req);

        // 2️⃣ Lưu tin nhắn user
        ChatMessage userMsg = messageMapper.toEntity(req);
        userMsg.setSession(session);
        userMsg.setSender(MessageSender.USER);
        userMsg.setType(MessageType.AI_GENERATED);
        messageRepository.save(userMsg);

        // 3️⃣ Kiểm tra FAQ trùng khớp
        List<FAQ> relatedFaqs = faqRepository.searchByKeyword(req.getContent());

        if (!relatedFaqs.isEmpty()) {
            // 3.1️⃣ Tạo danh sách FAQ cho AI chọn câu trả lời hợp lý nhất
            String faqList = relatedFaqs.stream()
                    .map(f -> "- " + f.getQuestion() + ": " + f.getAnswer())
                    .reduce("", (a, b) -> a + "\n" + b);

            var faqResponse = chatClient.prompt()
                    .system("""
                    Bạn là trợ lý ảo của rạp chiếu phim CNM Cinemas.
                    Dưới đây là danh sách các câu hỏi thường gặp (FAQ).
                    Hãy đọc danh sách này và chọn ra câu trả lời phù hợp nhất với câu hỏi người dùng.
                    Nếu không có câu nào thật sự phù hợp, hãy trả lời ngắn gọn rằng bạn không chắc và sẽ kiểm tra thêm.
                    """)
                    .user("Câu hỏi người dùng: " + req.getContent() + "\n\nDanh sách FAQ:\n" + faqList)
                    .call();

            String faqAnswer = faqResponse.content();
            if (faqAnswer != null && !faqAnswer.isBlank()) {
                String normalized = faqAnswer.toLowerCase();

                // Nếu AI nói mơ hồ → bỏ qua FAQ, chuyển xuống gọi tool
                if (normalized.contains("không chắc")
                        || normalized.contains("không rõ")
                        || normalized.contains("kiểm tra thêm")
                        || normalized.contains("không tìm thấy")
                        || normalized.contains("xin lỗi")) {
                    log.info("FAQ không chắc chắn, chuyển xuống xử lý bằng tool...");
                } else {
                    return chatMessageService.saveBotMessage(session, faqAnswer.trim(), MessageType.FAQ_STATIC);
                }
            }
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

        cinemaToolService.resetFlag();

        // 7️⃣ Gọi Spring AI (bản 1.0.3 chỉ có .user)
        var response = chatClient.prompt()
                .tools(cinemaToolService) // 👈 Cho phép model gọi các hàm @Tool
                .system(systemPrompt + "\n\nDưới đây là hội thoại trước đó:\n" + contextBuilder)
                .user(req.getContent())
                .call();

        String botReply = response.content();

        MessageType type = cinemaToolService.isToolUsed()
                ? MessageType.INTERNAL_API
                : MessageType.AI_GENERATED;

        // 8️⃣ Lưu phản hồi
        return chatMessageService.saveBotMessage(session,
                botReply != null ? botReply : "Xin lỗi, tôi hiện không trả lời được.",
                type);

    }
}