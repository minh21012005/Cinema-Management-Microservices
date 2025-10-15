package com.example.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiTestService {

    private final OpenAiChatModel chatModel;

    public AiTestService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String test() {
        return chatModel.call("Xin chào, bạn có đang hoạt động không?");
    }
}