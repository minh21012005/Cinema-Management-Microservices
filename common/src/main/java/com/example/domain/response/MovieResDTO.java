package com.example.domain.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieResDTO {
    private Long id;
    private String title;
    private String description;
    private String director;
    private String cast;
    private int durationInMinutes;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private boolean active;
    private String posterKey;
    private String trailerUrl; // Link trailer YouTube
    private List<String> categoryCodes;
    private List<String> categoryNames;
    private double ratingAvg;
    private int ratingCount;
}
