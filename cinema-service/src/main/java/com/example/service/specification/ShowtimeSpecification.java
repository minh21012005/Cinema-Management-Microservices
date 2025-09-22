package com.example.service.specification;

import com.example.domain.entity.Showtime;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ShowtimeSpecification {
    public static Specification<Showtime> findShowtimesWithFilters(
            Long cinemaId, Long roomId, List<Long> movieIds) {

        Specification<Showtime> spec = (root, query, cb) -> cb.conjunction();

        if (cinemaId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("room").get("cinema").get("id"), cinemaId));
        }

        if (roomId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("room").get("id"), roomId));
        }

        if (movieIds != null && !movieIds.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.get("movieId").in(movieIds));
        }

        return spec;
    }
}


