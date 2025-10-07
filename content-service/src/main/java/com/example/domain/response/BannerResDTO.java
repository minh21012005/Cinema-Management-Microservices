package com.example.domain.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerResDTO {

    private Long id;

    private String title;

    private String subtitle;

    private String imageKey;

    private String redirectUrl;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int displayOrder;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
