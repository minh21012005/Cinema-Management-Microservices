package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportMessageReadReqDTO {
    private Long messageId; // ID tin nhắn cần đánh dấu đã đọc
}
