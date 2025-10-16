package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "faqs")
public class FAQ extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    private String tags; // "payment,booking,refund"

    private String intent; // mapping với intent của chatbot

    private boolean active;
}
