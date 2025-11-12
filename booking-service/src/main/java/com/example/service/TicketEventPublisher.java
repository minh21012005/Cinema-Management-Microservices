package com.example.service;

import com.example.domain.entity.TicketEmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TicketEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishTicketPurchasedEvent(TicketEmailDTO ticketEmail) {
        try {
            String message = objectMapper.writeValueAsString(ticketEmail);
            kafkaTemplate.send("ticket-purchased", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize TicketPurchasedEvent", e);
        }
    }
}
