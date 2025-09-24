package com.example.controller;

import com.example.domain.entity.Category;
import com.example.domain.entity.Movie;
import com.example.domain.request.CategoryReqDTO;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.CategoryResDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.CategoryMapper;
import com.example.mapper.MovieMapper;
import com.example.service.CategoryService;
import com.example.service.MovieService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
