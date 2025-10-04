package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResDTO {
    private Long id;
    private Long staffId;
    private double totalAmount;
    private boolean paid;
    private String customerName;
    private String customerPhone;
    private LocalDateTime createdAt;

    private List<TicketRes> tickets;
    private List<ItemRes> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketRes {
        private Long ticketId;
        private Long seatId;
        private double price;
        private boolean paid;
        private boolean reserved;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRes {
        private Long itemId; // ID OrderItem
        private Long foodId; // Nếu là combo, có thể null
        private Long comboId; // Nếu là food, có thể null
        private int quantity;
        private double price;
    }
}
