package com.example.service.specification;

import com.example.domain.entity.Showtime;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

        if (movieIds != null) {
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

    public static Specification<Showtime> findShowtimesWithFilterForStaff(Long cinemaId, List<Long> movieIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // chỉ lấy showtime active
            predicates.add(cb.isTrue(root.get("active")));

            // chỉ lấy showtime trong ngày hôm nay
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
            predicates.add(cb.between(root.get("startTime"), startOfDay, endOfDay));

            // chỉ lấy showtime thuộc cinema hiện tại
            predicates.add(cb.equal(root.get("room").get("cinema").get("id"), cinemaId));

            // lọc theo movie nếu có
            if (movieIds != null) {
                predicates.add(root.get("movieId").in(movieIds));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


