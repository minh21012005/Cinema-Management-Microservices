package com.example.mapper;

import com.example.domain.entity.FAQ;
import com.example.domain.request.FaqReqDTO;
import com.example.domain.response.FaqResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FaqMapper extends BaseMapper<FAQ, FaqReqDTO, FaqResDTO> {

    @Override
    FaqResDTO toDto(FAQ entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FAQ toEntity(FaqReqDTO dto);
}
