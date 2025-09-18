package com.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "room_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomType extends BaseEntity<Long>{

    @Column(nullable = false, unique = true, length = 50)
    private String code; // Ví dụ: "STANDARD", "VIP", "IMAX"

    @Column(nullable = false, length = 100)
    private String name; // Tên hiển thị: "Phòng Standard", "Phòng IMAX"

    private String description; // Mô tả chi tiết (nếu cần)

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;
}
