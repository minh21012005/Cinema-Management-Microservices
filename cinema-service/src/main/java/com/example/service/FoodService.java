package com.example.service;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;

public interface FoodService extends BaseService<Food, Long, FoodReqDTO, FoodResDTO> {
}
