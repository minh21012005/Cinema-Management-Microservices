package com.example.service.impl;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import com.example.repository.FoodRepository;
import com.example.service.FoodService;

import org.springframework.stereotype.Service;


@Service
public class FoodServiceImpl
        extends BaseServiceImpl<Food, Long, FoodReqDTO, FoodResDTO>
        implements FoodService {

    private final FoodRepository foodRepository;

    protected FoodServiceImpl(FoodRepository foodRepository) {
        super(foodRepository);
        this.foodRepository = foodRepository;
    }
}
