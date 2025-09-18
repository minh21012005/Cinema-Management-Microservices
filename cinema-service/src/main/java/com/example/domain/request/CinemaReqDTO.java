package com.example.domain.request;

import lombok.Data;

@Data
public class CinemaReqDTO {
    private String name;
    private String address;
    private String city;
    private String phone;
    private boolean active;
}
