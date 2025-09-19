package com.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cinemas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends BaseEntity<Long>{

    @Column(nullable = false)
    private String name; // Tên cụm rạp

    @Column(nullable = false)
    private String address; // Địa chỉ rạp

    @Column(nullable = false)
    private String city; // Thành phố / tỉnh

    private String phone; // Số điện thoại liên hệ

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true; // true = đang hoạt động, false = ngừng hoạt động

    @JsonIgnore
    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;
}
