package com.example.domain.entity;

import com.example.util.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity<Long> {
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    private Long roleId;

    @Column(name = "cinema_id")
    private Long cinemaId;
}
