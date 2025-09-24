package com.example.service.specification;

import com.example.domain.entity.Movie;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovieSpecification {
    public static Specification<Movie> findMovieWithFilters(
            String title, Long categoryId, LocalDate fromDate, LocalDate toDate) {
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

        if (fromDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("releaseDate"), fromDate.atStartOfDay()));
        }

        if (toDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("releaseDate"), toDate.atTime(LocalTime.MAX)));
        }

        return spec;
    }
}
