package com.example.service;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface RatingService extends BaseService<Rating, Long, RatingReqDTO, RatingResDTO> {
    RatingResDTO create(RatingReqDTO dto) throws IdInvalidException;
    List<RatingResDTO> getRatingsByMovie(Long id) throws IdInvalidException;
}
