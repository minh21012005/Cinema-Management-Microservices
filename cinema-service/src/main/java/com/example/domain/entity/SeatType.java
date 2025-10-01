package com.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "seat_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatType extends BaseEntity<Long>{

    @Column(nullable = false)
    private String name; // Thường, VIP, Đôi, Sweetbox...

    @Column(nullable = false)
    private Double basePrice; // Giá cộng thêm so với vé thường

    @JsonIgnore
    @OneToMany(mappedBy = "seatType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}
