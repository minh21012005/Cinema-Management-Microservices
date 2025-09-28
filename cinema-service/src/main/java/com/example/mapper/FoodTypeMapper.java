package com.example.mapper;

import com.example.domain.entity.FoodType;
import com.example.domain.request.FoodTypeReqDTO;
import com.example.domain.response.FoodTypeResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodTypeMapper extends BaseMapper<FoodType, FoodTypeReqDTO, FoodTypeResDTO> {
}
