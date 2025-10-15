package com.example.service;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;

public interface RatingService extends BaseService<Rating, Long, RatingReqDTO, RatingResDTO> {
}
