package com.example.controller;

import com.example.domain.entity.Food;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.FoodMapper;
import com.example.service.FoodService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController extends BaseController<Food, Long, FoodReqDTO, FoodResDTO> {

    private final FoodService foodService;

    protected FoodController(FoodService foodService, FoodMapper foodMapper) {
        super(foodService, foodMapper);
        this.foodService = foodService;
    }

    @GetMapping("/all")
    @ApiMessage("Fetched all foods")
    @PreAuthorize("hasPermission(null, 'FOOD_VIEW')")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "typeId", required = false) Long typeId,
            Pageable pageable) {
        return ResponseEntity.ok(foodService.fetchAllFoods(name, typeId, pageable));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'FOOD_CREATE')")
    public ResponseEntity<FoodResDTO> create(FoodReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(dto));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'FOOD_UPDATE')")
    public ResponseEntity<FoodResDTO> update(@PathVariable("id") Long id, FoodReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(foodService.updateFood(id,dto));
    }
}
