package com.example.mapper;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper extends BaseMapper<Cinema, CinemaReqDTO, CinemaResDTO> {
}
