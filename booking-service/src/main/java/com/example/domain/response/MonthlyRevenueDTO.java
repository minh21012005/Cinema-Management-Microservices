package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRevenueDTO {
    private Integer month;
    private Double revenue;
    private Long tickets;
}