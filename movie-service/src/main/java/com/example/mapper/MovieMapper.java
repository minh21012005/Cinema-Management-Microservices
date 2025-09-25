package com.example.mapper;

import com.example.domain.entity.Category;
import com.example.domain.entity.Movie;
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
}
