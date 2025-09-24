package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "media_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaFile extends BaseEntity<Long> {

    @Column(nullable = false)
    private String type; // movie, food, drink, combo, ...

    @Column(nullable = false)
    private String originalFileName; // Tên file gốc

    @Column(nullable = false)
    private String objectKey; // key trong MinIO (movies/uuid-file.png)

    @Column(nullable = false)
    private String contentType; // image/png, image/jpeg

    @Column(nullable = false)
    private long size; // kích thước file (bytes)
}
