package com.example.domain.entity;

import com.example.domain.enums.SupportChatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "support_chat_sessions")
public class SupportChatSession extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String sessionId; // UUID duy nhất cho phiên CSKH

    private Long userId; // ID người dùng (lấy từ user-service)

    private Long agentId; // ID của nhân viên CSKH (từ admin-service hoặc staff-service)

    @Enumerated(EnumType.STRING)
    private SupportChatStatus status = SupportChatStatus.OPEN; // OPEN, ASSIGNED, CLOSED

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupportMessage> messages;
}
