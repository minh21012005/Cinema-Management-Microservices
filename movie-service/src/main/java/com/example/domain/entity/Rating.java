package com.example.domain.entity;

import com.example.domain.enums.RatingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends BaseEntity<Long> {

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int stars; // 1 đến 5 sao

    @Size(max = 1000)
    @Column(length = 1000)
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingStatus status = RatingStatus.PENDING;

    @NotNull
    @Column(nullable = false)
    private Long userId;
}
