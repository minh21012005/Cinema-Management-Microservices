package com.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEmailDTO {
    private String movieTitle;
    private String cinemaName;
    private String roomName;
    private LocalDateTime showtime;
    private List<String> seatCodes;
    private List<String> foods;
    private List<String> combos;
    private double totalPrice;
}
