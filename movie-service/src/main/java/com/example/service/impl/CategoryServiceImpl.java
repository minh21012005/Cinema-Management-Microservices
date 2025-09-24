package com.example.service.impl;

import com.example.domain.entity.Category;
import com.example.domain.request.CategoryReqDTO;
import com.example.domain.response.CategoryResDTO;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl
        extends BaseServiceImpl<Category, Long, CategoryReqDTO, CategoryResDTO>
        implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    protected CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResDTO> fetchAllActive() {
        return categoryRepository.findByActiveTrue().stream().map(
                categoryMapper::toDto
        ).toList();
    }
}
