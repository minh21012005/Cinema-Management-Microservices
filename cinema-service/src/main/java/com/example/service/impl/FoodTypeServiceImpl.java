package com.example.service.impl;

import com.example.domain.entity.FoodType;
import com.example.domain.request.FoodTypeReqDTO;
import com.example.domain.response.FoodTypeResDTO;
import com.example.mapper.FoodTypeMapper;
import com.example.repository.FoodTypeRepository;
import com.example.service.FoodTypeService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FoodTypeServiceImpl extends BaseServiceImpl<FoodType, Long, FoodTypeReqDTO, FoodTypeResDTO>
        implements FoodTypeService {

    private final FoodTypeRepository foodTypeRepository;
    private final FoodTypeMapper foodTypeMapper;

    protected FoodTypeServiceImpl(FoodTypeRepository foodTypeRepository,
                                  FoodTypeMapper foodTypeMapper) {
        super(foodTypeRepository);
        this.foodTypeRepository = foodTypeRepository;
        this.foodTypeMapper = foodTypeMapper;
    }


    @Override
    public List<FoodTypeResDTO> fetchAllFoodTypesActive() {
        return foodTypeRepository.findByActiveTrue().stream().map(
                foodTypeMapper::toDto
        ).toList(
        );
    }
}
