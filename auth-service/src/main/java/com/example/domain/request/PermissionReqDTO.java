package com.example.domain.request;

import lombok.Data;

@Data
public class PermissionReqDTO {
    private String code; // MOVIE_CREATE, BOOKING_MANAGE...
    private String name;
    private String method;
    private String apiPath;
    private String module;
    private String description; // optional
}
