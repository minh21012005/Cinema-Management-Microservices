package com.example.domain.response;

import com.example.domain.entity.SeatType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResSeatDTO {
    private Long id;
    private String name; // Ví dụ: A1, B5...
    private boolean active;
    private SeatType seatType;
    private Integer rowIndex;
    private Integer colIndex;
}