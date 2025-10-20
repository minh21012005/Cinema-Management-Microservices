package com.example.domain.response;

import com.example.domain.enums.SupportMessageSender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportMessageResDTO {
    private Long id;
    private String sessionId;
    private String content;
    private SupportMessageSender sender; // USER hoặc AGENT
    private String createdAt;
    private String readAt;
}
