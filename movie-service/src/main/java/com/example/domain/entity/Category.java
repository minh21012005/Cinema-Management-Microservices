package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long>{

    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany(mappedBy = "categories")
    private List<Movie> movies;
}