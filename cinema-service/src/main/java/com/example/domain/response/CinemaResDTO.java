package com.example.domain.response;

import lombok.Data;

@Data
public class CinemaResDTO {
    private String name;
    private String address;
    private String city;
    private String phone;
    private boolean active;
}
