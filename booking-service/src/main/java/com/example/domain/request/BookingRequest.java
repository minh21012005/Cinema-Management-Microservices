package com.example.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private Long showtimeId;
    private List<SeatDTO> seats;
    private List<FoodDTO> foods;
    private List<ComboDTO> combos;
    private String customerName;
    private String customerPhone;

    @Data
    public static class SeatDTO {
        private Long seatId;
        private double price;
    }

    @Data
    public static class FoodDTO {
        private Long foodId;
        private int quantity;
        private double price;
    }

    @Data
    public static class ComboDTO {
        private Long comboId;
        private int quantity;
        private double price;
    }
}

