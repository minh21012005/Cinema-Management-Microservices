package com.example.domain.entity;

import com.example.domain.request.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEmailDTO {
    private String email;
    private String movieTitle;
    private String cinemaName;
    private String roomName;
    private LocalDateTime showtime;
    private List<String> seatCodes;
    private List<ItemDTO> foods;
    private List<ItemDTO> combos;
    private double totalPrice;
}
