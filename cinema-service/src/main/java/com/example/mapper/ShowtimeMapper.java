package com.example.mapper;

import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.ShowtimeResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper extends BaseMapper<Showtime, ShowtimeReqDTO, ShowtimeResDTO> {
    @Override
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomName", source = "room.name")
    ShowtimeResDTO toDto(Showtime entity);
}
