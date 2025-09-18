package com.example.mapper;

public interface BaseMapper<E, Req, Res> {
    Res toDto(E entity);
    E toEntity(Req dto);
}

