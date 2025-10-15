package com.example.controller;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.mapper.RatingMapper;
import com.example.service.RatingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController extends BaseController<Rating, Long, RatingReqDTO, RatingResDTO> {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService, RatingMapper ratingMapper) {
        super(ratingService, ratingMapper);
        this.ratingService = ratingService;
    }
}
