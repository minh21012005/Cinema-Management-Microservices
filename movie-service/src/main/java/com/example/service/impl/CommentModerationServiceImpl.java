package com.example.service.impl;

import com.example.service.CommentModerationService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class CommentModerationServiceImpl implements CommentModerationService {
    private final OpenAiChatModel chatModel;

    public CommentModerationServiceImpl(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public boolean isCommentSafe(String comment) {
        String prompt = """
                Bạn là hệ thống kiểm duyệt bình luận.
                Hãy xem bình luận sau có chứa từ ngữ thô tục, xúc phạm, phân biệt, hoặc không phù hợp không.
                Chỉ trả lời "YES" nếu bình luận là hợp lệ, hoặc "NO" nếu bình luận vi phạm.
                
                Bình luận: "%s"
                """.formatted(comment);

        String result = chatModel.call(prompt).trim().toLowerCase();
        return result.contains("yes");
    }
}
