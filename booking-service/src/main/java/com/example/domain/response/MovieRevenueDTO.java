package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieRevenueDTO {
    private String title;
    private double revenue;  // tổng doanh thu
}