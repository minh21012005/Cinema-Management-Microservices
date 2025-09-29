package com.example.mapper;

import com.example.domain.entity.ComboFood;
import com.example.domain.response.ComboResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComboFoodMapper {

    @Mapping(target = "foodId", source = "food.id")
    @Mapping(target = "foodName", source = "food.name")
    @Mapping(target = "quantity", source = "quantity")
    ComboResDTO.ComboFoodRes toDto(ComboFood entity);
}

