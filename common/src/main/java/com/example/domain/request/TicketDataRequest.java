package com.example.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class TicketDataRequest {
    private List<Long> seatIds;
    private List<ItemDTO> foods;
    private List<ItemDTO> combos;
}
