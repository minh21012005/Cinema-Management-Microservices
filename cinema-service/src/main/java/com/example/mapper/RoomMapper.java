package com.example.mapper;

import com.example.domain.entity.Room;
import com.example.domain.entity.RoomType;
import com.example.domain.request.RoomReqDTO;
import com.example.domain.response.RoomResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper extends BaseMapper<Room, RoomReqDTO, RoomResDTO> {
    @Override
    @Mapping(source = "roomType", target = "type") // map RoomType -> nested DTO
    RoomResDTO toDto(Room entity);

    // map RoomType entity -> RoomResDTO.Type
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    RoomResDTO.Type toType(RoomType roomType);
}
