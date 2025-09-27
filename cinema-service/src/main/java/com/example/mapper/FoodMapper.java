package com.example.mapper;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper extends BaseMapper<Food, FoodReqDTO, FoodResDTO> {
    // Map từ entity -> response
    @Override
    @Mapping(source = "type.name", target = "typeName")
    FoodResDTO toDto(Food food);

    // Map từ request -> entity
    @Override
    @Mapping(source = "typeId", target = "type.id")
    Food toEntity(FoodReqDTO dto);
}
