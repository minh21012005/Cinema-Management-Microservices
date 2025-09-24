package com.example.service;

import com.example.domain.entity.Category;
import com.example.domain.request.CategoryReqDTO;
import com.example.domain.response.CategoryResDTO;

import java.util.List;

public interface CategoryService extends BaseService<Category, Long, CategoryReqDTO, CategoryResDTO> {
    List<CategoryResDTO> fetchAllActive();
}
