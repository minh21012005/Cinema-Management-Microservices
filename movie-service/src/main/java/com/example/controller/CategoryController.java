package com.example.controller;

import com.example.domain.entity.Category;
import com.example.domain.request.CategoryReqDTO;
import com.example.domain.response.CategoryResDTO;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController<Category, Long, CategoryReqDTO, CategoryResDTO> {

    private final CategoryService categoryService;

    protected CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        super(categoryService, categoryMapper);
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasPermission(null, 'CATEGORY_VIEW')")
    public ResponseEntity<List<CategoryResDTO>> findAllActive(){
        return ResponseEntity.ok(categoryService.fetchAllActive());
    }
 }
