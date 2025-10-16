package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageReqDTO {
    private String sessionId;  // xác định phiên chat
    private String content;    // nội dung người dùng gửi
}
