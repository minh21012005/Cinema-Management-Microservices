package com.example.domain.entity;

import com.example.domain.enums.RatingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends BaseEntity<Long> {

    @Column(nullable = false)
    private int stars; // 1 đến 5 sao

    @Column(length = 1000)
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingStatus status = RatingStatus.PENDING;

    @Column(nullable = false)
    private Long userId;
}
