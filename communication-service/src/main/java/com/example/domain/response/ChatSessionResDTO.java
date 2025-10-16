package com.example.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSessionResDTO {
    private Long id;
    private String sessionId;
    private Long userId;
    private String createdAt;
    private String updatedAt;
}
