package com.example.service.specification;

import com.example.domain.entity.Showtime;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShowtimeSpecification {
    public static Specification<Showtime> findShowtimesWithFilters(
            Long cinemaId, Long roomId, List<Long> movieIds, LocalDate fromDate, LocalDate toDate) {

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

        if (fromDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("startTime"), fromDate.atStartOfDay()));
        }

        if (toDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("startTime"), toDate.atTime(LocalTime.MAX)));
        }

        return spec;
    }
}


