package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = { "room_id", "row_index", "col_index" })
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends BaseEntity<Long>{

    // Ghế sẽ xác định theo row/col thay vì chỉ name
    @Column(nullable = false)
    private Integer rowIndex;

    @Column(nullable = false)
    private Integer colIndex;

    // Mã ghế (A1, B5...), FE có thể generate
    private String name;

    @Column(nullable = false)
    private boolean active = true; // true = dùng được, false = không sử dụng

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;
}
