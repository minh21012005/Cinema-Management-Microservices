package com.example.controller;

import com.example.domain.entity.FoodType;
import com.example.domain.request.FoodTypeReqDTO;
import com.example.domain.response.FoodTypeResDTO;
import com.example.mapper.FoodTypeMapper;
import com.example.service.FoodTypeService;
import com.example.util.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foodtypes")
public class FoodTypeController extends BaseController<FoodType, Long, FoodTypeReqDTO, FoodTypeResDTO> {

    private final FoodTypeService foodTypeService;

    protected FoodTypeController(FoodTypeService foodTypeService, FoodTypeMapper foodTypeMapper) {
        super(foodTypeService, foodTypeMapper);
        this.foodTypeService = foodTypeService;
    }

    @GetMapping("/active")
    @ApiMessage("Fetched all food types")
    @PreAuthorize("hasPermission(null, 'FOODTYPE_VIEW')")
    public ResponseEntity<List<FoodTypeResDTO>> getAllActiveFoodTypes() {
        return ResponseEntity.ok(foodTypeService.fetchAllFoodTypesActive());
    }
}
