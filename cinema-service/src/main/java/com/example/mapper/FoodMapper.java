package com.example.mapper;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper extends BaseMapper<Food, FoodReqDTO, FoodResDTO> {
}
