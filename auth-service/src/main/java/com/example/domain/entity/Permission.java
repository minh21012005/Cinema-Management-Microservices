package com.example.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity<Long> {
    @Column(nullable = false, unique = true)
    private String code; // MOVIE_CREATE, BOOKING_MANAGE...

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String apiPath;

    @Column(nullable = false)
    private String module;

    private String description; // optional

    @Column(nullable = false)
    private boolean active = true;
}
