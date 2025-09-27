package com.example.service;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface FoodService extends BaseService<Food, Long, FoodReqDTO, FoodResDTO> {
    ResultPaginationDTO fetchAllFoods(String name, Long typeId, Pageable pageable);
    FoodResDTO createFood(FoodReqDTO dto) throws IdInvalidException;
}
