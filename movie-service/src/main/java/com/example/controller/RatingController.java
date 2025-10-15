package com.example.controller;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.mapper.RatingMapper;
import com.example.service.RatingService;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController extends BaseController<Rating, Long, RatingReqDTO, RatingResDTO> {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService, RatingMapper ratingMapper) {
        super(ratingService, ratingMapper);
        this.ratingService = ratingService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'RATING_CREATE')")
    public ResponseEntity<RatingResDTO> create(@Valid @RequestBody RatingReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(dto));
    }

    @GetMapping("/movies/{id}")
    @PreAuthorize("hasPermission(null, 'RATING_VIEW')")
    public ResponseEntity<List<RatingResDTO>> getRatingsByMovie(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(ratingService.getRatingsByMovie(id));
    }
}
