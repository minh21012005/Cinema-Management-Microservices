package com.example.domain.response;

import com.example.domain.enums.SupportChatStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportChatSessionResDTO {
    private Long id;
    private String sessionId;
    private Long userId;
    private String customerName;
    private Long agentId;
    private SupportChatStatus status;
    private String lastMessage;
    private String createdAt;
    private String updatedAt;
    private int unreadCountForAgent;
}
