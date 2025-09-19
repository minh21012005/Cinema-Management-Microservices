package com.example.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, Req, Res> {
    Res toDto(E entity);
    E toEntity(Req dto);
    void updateEntityFromDto(Req dto, @MappingTarget E entity);
}

