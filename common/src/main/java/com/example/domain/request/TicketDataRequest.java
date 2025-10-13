package com.example.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class TicketDataRequest {
    private List<Long> seatIds;
    private List<Long> foodIds;
    private List<Long> comboIds;
}
