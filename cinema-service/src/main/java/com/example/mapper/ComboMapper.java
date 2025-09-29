package com.example.mapper;

import com.example.domain.entity.Combo;
import com.example.domain.request.ComboReqDTO;
import com.example.domain.response.ComboResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ComboFoodMapper.class})
public interface ComboMapper extends BaseMapper<Combo, ComboReqDTO, ComboResDTO> {

    @Override
    @Mapping(target = "foods", source = "comboFoods") // map list
    ComboResDTO toDto(Combo entity);

    @Override
    @Mapping(target = "comboFoods", ignore = true) // xử lý trong service
    Combo toEntity(ComboReqDTO dto);

    @Override
    @Mapping(target = "comboFoods", ignore = true) // xử lý trong service
    void updateEntityFromDto(ComboReqDTO dto, @MappingTarget Combo entity);
}

