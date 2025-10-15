package com.example.service.impl;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.mapper.RatingMapper;
import com.example.repository.RatingRepository;
import com.example.service.RatingService;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl
        extends BaseServiceImpl<Rating, Long, RatingReqDTO, RatingResDTO>
        implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    protected RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        super(ratingRepository);
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }
}
