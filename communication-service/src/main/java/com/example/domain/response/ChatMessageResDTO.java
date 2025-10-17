package com.example.domain.response;

import com.example.domain.enums.MessageSender;
import com.example.domain.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResDTO {
    private Long id;
    private String content;
    private MessageSender sender;
    private MessageType type;
    private String createdAt;
    private String sessionId;
}
