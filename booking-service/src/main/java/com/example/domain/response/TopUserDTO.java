package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TopUserDTO {
    private Long id;
    private String name;
    private Long tickets;
    private Double spending;
    private String tier;
}
