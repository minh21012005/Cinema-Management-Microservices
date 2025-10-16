package com.example.domain.entity;

import com.example.domain.enums.MessageSender;
import com.example.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    @Enumerated(EnumType.STRING)
    private MessageSender sender; // USER hoặc BOT

    @Enumerated(EnumType.STRING)
    private MessageType type; // FAQ_STATIC, INTERNAL_API, AI_GENERATED

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String intent; // ví dụ: "booking_status", "refund_request", "showtime_today"

    private boolean resolved = false; // true nếu chatbot đã xử lý xong

    private String relatedEntity; // ví dụ: "MOVIE", "BOOKING", "ORDER"
    private Long relatedId;       // id của entity nếu có (vd: movieId = 12)
}
