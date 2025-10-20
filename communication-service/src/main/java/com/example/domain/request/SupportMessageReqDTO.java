package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportMessageReqDTO {
    private String sessionId;   // ID phiên chat (UUID)
    private String content;     // nội dung tin nhắn
}
