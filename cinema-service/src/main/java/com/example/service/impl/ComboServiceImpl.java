package com.example.service.impl;

import com.example.domain.entity.Combo;
import com.example.domain.entity.ComboFood;
import com.example.domain.entity.ComboFoodId;
import com.example.domain.entity.Food;
import com.example.domain.request.ComboReqDTO;
import com.example.domain.response.ComboResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.ComboMapper;
import com.example.repository.ComboRepository;
import com.example.repository.FoodRepository;
import com.example.service.ComboService;
import com.example.service.specification.ComboSpecification;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComboServiceImpl
        extends BaseServiceImpl<Combo, Long, ComboReqDTO, ComboResDTO>
        implements ComboService {

    private final ComboRepository comboRepository;
    private final ComboMapper comboMapper;
    private final FoodRepository foodRepository;

    protected ComboServiceImpl(ComboRepository comboRepository,
                               ComboMapper comboMapper,
                               FoodRepository foodRepository) {
        super(comboRepository);
        this.comboRepository = comboRepository;
        this.comboMapper = comboMapper;
        this.foodRepository = foodRepository;
    }

    @Override
    public ComboResDTO create(ComboReqDTO dto) {
        String newCode = generateComboCode();

        // 2. Map DTO -> Entity (chưa có comboFoods)
        Combo combo = comboMapper.toEntity(dto);
        combo.setCode(newCode);

        // 3. Xử lý foods trong DTO
        List<ComboFood> comboFoods = new ArrayList<>();
        if (dto.getFoods() != null) {
            for (ComboReqDTO.ComboFoodItem item : dto.getFoods()) {
                Food food = foodRepository.findById(item.getFoodId())
                        .orElseThrow(() -> new RuntimeException("Food not found: " + item.getFoodId()));

                ComboFood comboFood = ComboFood.builder()
                        .id(new ComboFoodId(null, food.getId())) // comboId sẽ set sau khi persist
                        .combo(combo)
                        .food(food)
                        .quantity(item.getQuantity())
                        .build();

                comboFoods.add(comboFood);
            }
        }
        combo.setComboFoods(comboFoods);

        // 4. Lưu combo
        Combo saved = comboRepository.save(combo);

        // 5. Map sang DTO trả về
        return comboMapper.toDto(saved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComboResDTO update(Long id, ComboReqDTO dto) throws IdInvalidException {
        // 1. Tìm combo
        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Combo not found with id: " + id));

        // 2. Cập nhật các field cơ bản
        combo.setName(dto.getName());
        combo.setPrice(dto.getPrice());
        combo.setDescription(dto.getDescription());
        combo.setAvailable(dto.isAvailable());
        combo.setImageKey(dto.getImageKey());

        // 3. Xử lý danh sách foods
        // Xóa danh sách cũ trước (cascade = ALL, orphanRemoval = true nên tự xóa trong DB)
        combo.getComboFoods().clear();

        if (dto.getFoods() != null) {
            for (ComboReqDTO.ComboFoodItem item : dto.getFoods()) {
                Food food = foodRepository.findById(item.getFoodId())
                        .orElseThrow(() -> new RuntimeException("Food not found: " + item.getFoodId()));

                ComboFood comboFood = ComboFood.builder()
                        .id(new ComboFoodId(combo.getId(), food.getId()))
                        .combo(combo)
                        .food(food)
                        .quantity(item.getQuantity())
                        .build();

                combo.getComboFoods().add(comboFood); // ✅ thêm trực tiếp vào list gốc
            }
        }

        // 4. Lưu thay đổi
        Combo updated = comboRepository.save(combo);

        // 5. Trả về DTO
        return comboMapper.toDto(updated);
    }

    @Override
    public ResultPaginationDTO fetchAllCombos(String name, Pageable pageable) {
        Page<Combo> pageCombo = this.comboRepository.findAll(
                ComboSpecification.findComboWithFilters(name), pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCombo.getTotalPages());
        mt.setTotal(pageCombo.getTotalElements());

        rs.setMeta(mt);

        // map entity -> dto
        List<ComboResDTO> listCombo = pageCombo.getContent()
                .stream()
                .map(comboMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listCombo);

        return rs;
    }

    @Override
    public List<ComboResDTO> fetchAllCombosActive() {
        return comboRepository.findByAvailableTrue()
                .stream()
                .map(comboMapper::toDto)
                .toList();
    }

    public String generateComboCode() {
        // Ví dụ: CB + số tăng dần
        Long count = comboRepository.count() + 1;
        return String.format("CB%03d", count); // CB001, CB002, ...
    }
}
