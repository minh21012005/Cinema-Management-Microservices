package com.example.service.specification;

import com.example.domain.entity.Banner;
import org.springframework.data.jpa.domain.Specification;

public class BannerSpecification {
    public static Specification<Banner> findBannersWithFilters(String title) {
        Specification<Banner> spec = (root, query, cb) -> cb.conjunction(); // mặc định luôn true

        if (title != null && !title.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        return spec;
    }
}
