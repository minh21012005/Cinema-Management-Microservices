package com.example.domain.response;

import com.example.domain.entity.SeatType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatStatusDTO {
    private Long id;
    private String name;
    private Integer rowIndex;
    private Integer colIndex;
    private SeatType seatType; // "THUONG", "VIP", "DOI"
    private boolean booked;  // ghế đã được đặt
}

