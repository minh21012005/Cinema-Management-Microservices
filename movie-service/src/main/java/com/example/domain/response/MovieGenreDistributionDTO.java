package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieGenreDistributionDTO {
    private String name;
    private Double value;
}
