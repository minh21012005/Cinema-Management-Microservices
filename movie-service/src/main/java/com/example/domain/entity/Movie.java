package com.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title; // Tên phim

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả phim

    @Column(nullable = false)
    private int durationInMinutes; // Thời lượng (phút)

    @Column(nullable = false)
    private LocalDate releaseDate; // Ngày phát hành

    private LocalDate endDate; // Ngày kết thúc chiếu (có thể null nếu chưa xác định)

    @Column(nullable = false)
    private boolean active = true; // Trạng thái phim

    @Column(nullable = false)
    private String poster;

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;
}
