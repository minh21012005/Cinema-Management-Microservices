package com.example.mapper;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper extends BaseMapper<Rating, RatingReqDTO, RatingResDTO> {

    @Override
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "dd/MM/yyyy")
    RatingResDTO toDto(Rating entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Rating toEntity(RatingReqDTO dto);
}
