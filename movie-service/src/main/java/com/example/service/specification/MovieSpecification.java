package com.example.service.specification;

import com.example.domain.entity.Movie;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {
    public static Specification<Movie> findMovieWithFilters(String title, Long categoryId) {
        Specification<Movie> spec = (root, query, cb) -> cb.conjunction(); // mặc định luôn true

        if (title != null && !title.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> {
                // join sang categories
                Join<Object, Object> categories = root.join("categories", JoinType.INNER);
                return cb.equal(categories.get("id"), categoryId);
            });
        }

        return spec;
    }
}
