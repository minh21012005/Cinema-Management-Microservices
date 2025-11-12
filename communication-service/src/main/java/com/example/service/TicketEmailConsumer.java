package com.example.service;

import com.example.domain.entity.TicketEmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketEmailConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public TicketEmailConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "ticket-purchased", groupId = "communication-service-group")
    public void consume(String message) {
        try {
            TicketEmailDTO event = objectMapper.readValue(message, TicketEmailDTO.class);
            emailService.sendTicketEmail(event);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi consume Kafka message: " + e.getMessage());
        }
    }
}