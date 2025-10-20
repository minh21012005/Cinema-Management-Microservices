package com.example.domain.entity;

import com.example.domain.enums.SupportMessageSender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "support_messages")
public class SupportMessage extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private SupportChatSession session;

    @Enumerated(EnumType.STRING)
    private SupportMessageSender sender; // USER hoặc AGENT

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime readAt; // null nếu chưa đọc
}
