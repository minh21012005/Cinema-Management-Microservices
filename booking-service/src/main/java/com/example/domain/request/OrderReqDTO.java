package com.example.domain.request;

import com.example.domain.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class OrderReqDTO {
    private Long showtimeId;
    private List<BookingRequest.SeatDTO> seats;
    private List<BookingRequest.FoodDTO> foods;
    private List<BookingRequest.ComboDTO> combos;
    private String customerName;
    private String customerPhone;
    private PaymentMethod paymentMethod;

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
