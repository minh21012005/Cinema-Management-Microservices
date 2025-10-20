package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportChatSessionReqDTO {
    private String sessionId;   // Có thể gửi từ frontend (UUID)
    private Long userId;        // ID người dùng (bắt buộc)
    private Long agentId;       // ID agent (backend sẽ gán khi assign)
}
