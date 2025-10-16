package com.example.domain.enums;

public enum MessageType {
    FAQ_STATIC,     // câu hỏi tĩnh trong bảng FAQ
    INTERNAL_API,   // gọi API nội bộ, ví dụ: showtime, booking, user info
    AI_GENERATED    // câu trả lời từ OpenAI
}
