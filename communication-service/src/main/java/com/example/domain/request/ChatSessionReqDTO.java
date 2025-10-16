package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSessionReqDTO {
    private String sessionId;   // Có thể gửi từ frontend (ví dụ UUID)
    private Long userId;        // ID người dùng (nếu đã đăng nhập)
}
