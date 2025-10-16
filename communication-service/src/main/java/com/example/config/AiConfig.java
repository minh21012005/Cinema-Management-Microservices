package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        // Builder này sẽ tự phát hiện và enable chế độ enterprise function calling
        return ChatClient.builder(openAiChatModel).build();
    }
}
