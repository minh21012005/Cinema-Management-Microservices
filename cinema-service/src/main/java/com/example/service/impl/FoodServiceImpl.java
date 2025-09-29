package com.example.service.impl;

import com.example.domain.entity.Food;
import com.example.domain.entity.FoodType;
import com.example.domain.request.FoodReqDTO;
import com.example.domain.response.FoodResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.FoodMapper;
import com.example.repository.FoodRepository;
import com.example.repository.FoodTypeRepository;
import com.example.service.FoodService;
import com.example.service.specification.FoodSpecification;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FoodServiceImpl
        extends BaseServiceImpl<Food, Long, FoodReqDTO, FoodResDTO>
        implements FoodService {

    private final FoodRepository foodRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final FoodMapper foodMapper;

    protected FoodServiceImpl(FoodRepository foodRepository, FoodMapper foodMapper,
                              FoodTypeRepository foodTypeRepository) {
        super(foodRepository);
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
        this.foodTypeRepository = foodTypeRepository;
    }

    @Override
    public ResultPaginationDTO fetchAllFoods(String name, Long typeId, Pageable pageable) {
        Page<Food> pageFood = this.foodRepository.findAll(
                FoodSpecification.findFoodWithFilters(name, typeId), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageFood.getTotalPages());
        mt.setTotal(pageFood.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<FoodResDTO> listFood = pageFood.getContent()
                .stream().map(foodMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listFood);

        return rs;
    }

    @Override
    public FoodResDTO createFood(FoodReqDTO dto) throws IdInvalidException {
        // Validate code
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new IdInvalidException("Food code is required");
        }
        if (foodRepository.existsByCode(dto.getCode())) {
            throw new IdInvalidException("Food code already exists");
        }

        // Validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IdInvalidException("Food name is required");
        }

        // Validate price
        if (dto.getPrice() == null || dto.getPrice() <= 0) {
            throw new IdInvalidException("Food price must be greater than 0");
        }

        // Validate imageKey
        if (dto.getImageKey() == null || dto.getImageKey().trim().isEmpty()) {
            throw new IdInvalidException("Food image is required");
        }

        // Validate typeId
        if (dto.getTypeId() == null) {
            throw new IdInvalidException("Food typeId is required");
        }
        FoodType type = foodTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new IdInvalidException("Food type not found"));

        // Build entity
        Food food = foodMapper.toEntity(dto);
        food.setType(type); // gán entity đầy đủ, có cả name
        foodRepository.save(food);

        return foodMapper.toDto(food);
    }

    @Override
    public FoodResDTO updateFood(Long id, FoodReqDTO dto) throws IdInvalidException {
        // Validate id
        if (id == null) {
            throw new IdInvalidException("Food id is required for update");
        }

        // Tìm món ăn cần update
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Food not found with id: " + id));

        // Validate code
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new IdInvalidException("Food code is required");
        }
        // Nếu đổi code thì check trùng
        if (!dto.getCode().equals(food.getCode()) && foodRepository.existsByCode(dto.getCode())) {
            throw new IdInvalidException("Food code already exists");
        }

        // Validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IdInvalidException("Food name is required");
        }

        // Validate price
        if (dto.getPrice() == null || dto.getPrice() <= 0) {
            throw new IdInvalidException("Food price must be greater than 0");
        }

        // Validate imageKey
        if (dto.getImageKey() == null || dto.getImageKey().trim().isEmpty()) {
            throw new IdInvalidException("Food image is required");
        }

        // Validate typeId
        if (dto.getTypeId() == null) {
            throw new IdInvalidException("Food typeId is required");
        }
        FoodType type = foodTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new IdInvalidException("Food type not found"));

        // Update các field
        food.setCode(dto.getCode());
        food.setName(dto.getName());
        food.setPrice(dto.getPrice());
        food.setDescription(dto.getDescription());
        food.setImageKey(dto.getImageKey());
        food.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        food.setType(type);

        // Lưu lại
        foodRepository.save(food);

        return foodMapper.toDto(food);
    }

    @Override
    public List<FoodResDTO> fetchAllFoodsActive() {
        return foodRepository.findByAvailableTrue()
                .stream()
                .map(foodMapper::toDto)
                .toList();
    }
}
