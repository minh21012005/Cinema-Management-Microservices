package com.example.service;

import com.example.domain.entity.FoodType;
import com.example.domain.request.FoodTypeReqDTO;
import com.example.domain.response.FoodTypeResDTO;

import java.util.List;

public interface FoodTypeService extends BaseService<FoodType, Long, FoodTypeReqDTO, FoodTypeResDTO> {
    List<FoodTypeResDTO> fetchAllFoodTypesActive();
}
