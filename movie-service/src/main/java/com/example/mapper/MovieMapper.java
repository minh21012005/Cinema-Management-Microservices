package com.example.mapper;

import com.example.domain.entity.Category;
import com.example.domain.entity.Movie;
import com.example.domain.entity.Rating;
import com.example.domain.enums.RatingStatus;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper extends BaseMapper<Movie, MovieReqDTO, MovieResDTO> {
    @Override
    @Mapping(target = "categoryCodes", expression = "java(mapCategoriesToCodes(movie.getCategories()))")
    @Mapping(target = "categoryNames", expression = "java(mapCategoriesToNames(movie.getCategories()))")
    @Mapping(target = "ratingAvg", expression = "java(calculateRatingAvg(movie.getRatings()))")
    @Mapping(target = "ratingCount", expression = "java(calculateRatingCount(movie.getRatings()))")
    MovieResDTO toDto(Movie movie);

    // helper method cho MapStruct gọi
    default List<String> mapCategoriesToCodes(List<Category> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(Category::getCode)
                .collect(Collectors.toList());
    }

    // helper method cho MapStruct gọi
    default List<String> mapCategoriesToNames(List<Category> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    default double calculateRatingAvg(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        List<Rating> approved = ratings.stream()
                .filter(r -> r.getStatus() == RatingStatus.APPROVED)
                .toList();
        if (approved.isEmpty()) return 0.0;
        double avg = approved.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
        return Math.round(avg * 10.0) / 10.0;
    }

    default int calculateRatingCount(List<Rating> ratings) {
        if (ratings == null) return 0;
        return (int) ratings.stream()
                .filter(r -> r.getStatus() == RatingStatus.APPROVED)
                .count();
    }
}
