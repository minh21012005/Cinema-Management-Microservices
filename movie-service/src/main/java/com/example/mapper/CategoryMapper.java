package com.example.mapper;

import com.example.domain.entity.Category;
import com.example.domain.request.CategoryReqDTO;
import com.example.domain.response.CategoryResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryReqDTO, CategoryResDTO> {
}
