package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_sessions")
public class ChatSession extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String sessionId; // UUID duy nhất cho từng phiên chat

    private Long userId; // Nếu có đăng nhập thì lưu, nếu guest thì null

    private boolean active = true; // đánh dấu session còn hoạt động

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> messages;
}
