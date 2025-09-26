package com.example.controller;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import com.example.mapper.FoodMapper;
import com.example.service.FoodService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController extends BaseController<Food, Long, FoodReqDTO, FoodResDTO> {

    private final FoodService foodService;

    protected FoodController(FoodService foodService, FoodMapper foodMapper) {
        super(foodService, foodMapper);
        this.foodService = foodService;
    }

}
