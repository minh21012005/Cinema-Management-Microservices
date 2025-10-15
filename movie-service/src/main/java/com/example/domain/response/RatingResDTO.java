package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResDTO {
    private Long id;
    private int stars;
    private String comment;
    private String status;
    private String movieTitle;
    private String username;
    private String createdAt;
}
