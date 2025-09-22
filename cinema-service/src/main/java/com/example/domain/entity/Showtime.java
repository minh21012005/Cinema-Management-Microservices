package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime extends BaseEntity<Long>{

    @Column(nullable = false)
    private LocalDateTime startTime; // Thời gian bắt đầu chiếu

    @Column(nullable = false)
    private LocalDateTime endTime;   // Thời gian kết thúc chiếu

    @Column(nullable = false)
    private boolean active = true; // Trạng thái suất chiếu

    @Column(name = "movie_id")
    private Long movieId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}
